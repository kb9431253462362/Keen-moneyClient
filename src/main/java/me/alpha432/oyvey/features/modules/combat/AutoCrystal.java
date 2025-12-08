package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.ender.EndCrystal; // <<< FIXED IMPORT
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item; // <<< MISSING IMPORT
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket; // Added for correct slot switch
import java.util.Comparator;
import java.util.List;

public class AutoCrystal extends Module {

    private final Setting<Double> placeRange = num("PlaceRange", 5.0, 1.0, 6.0);
    private final Setting<Double> breakRange = num("BreakRange", 4.0, 1.0, 6.0);
    private final Setting<Boolean> antiWeakness = bool("AntiWeakness", true);
    
    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks end crystals.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        doBreak();
        doPlace();
    }

    private void doBreak() {
        // NOTE: The `mc.level.getEntities()` call on the next line is likely an accessor/mixin which may need fixing
        // depending on your client's structure, but I will not change it as it is common in some clients.
        List<? extends Entity> entities = mc.level.getEntities(null, new AABB(mc.player.blockPosition().offset(-10, -10, -10), mc.player.blockPosition().offset(10, 10, 10)));
        Entity crystalToBreak = entities.stream()
            .filter(entity -> entity instanceof EndCrystal)
            .filter(entity -> mc.player.distanceTo(entity) <= breakRange.getValue())
            .min(Comparator.comparingDouble(mc.player::distanceTo))
            .orElse(null);

        if (crystalToBreak != null) {
            mc.gameMode.attack(mc.player, crystalToBreak);
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private void doPlace() {
        BlockPos placePos = findObsidianPlacePos();

        if (placePos != null) {
            int crystalSlot = findItemInHotbar(Items.END_CRYSTAL);
            
            if (crystalSlot != -1) {
                int oldSlot = mc.player.getInventory().selected;
                
                // Fix: Use packet to switch slot on server, then set client-side.
                // The private 'selected' access is fixed here by changing it back after sending the packet.
                mc.player.connection.send(new ServerboundSetCarriedItemPacket(crystalSlot));
                mc.player.getInventory().selected = crystalSlot; 
                
                mc.gameMode.useItemOn(
                    mc.player,
                    InteractionHand.MAIN_HAND,
                    new BlockHitResult(
                        Vec3.atCenterOf(placePos),
                        Direction.UP,
                        placePos,
                        false
                    )
                );

                // Fix: Revert to the old slot using the same method.
                mc.player.connection.send(new ServerboundSetCarriedItemPacket(oldSlot));
                mc.player.getInventory().selected = oldSlot;
            }
        }
    }

    private BlockPos findObsidianPlacePos() {
        BlockPos playerPos = mc.player.blockPosition();

        for (int x = (int) -placeRange.getValue(); x <= placeRange.getValue(); x++) {
            for (int y = (int) -placeRange.getValue(); y <= placeRange.getValue(); y++) {
                for (int z = (int) -placeRange.getValue(); z <= placeRange.getValue(); z++) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    
                    if (mc.level.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.level.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
                        BlockPos crystalPos = pos.above();
                        
                        if (mc.level.getBlockState(crystalPos).isAir() && mc.level.getBlockState(pos.above(2)).isAir()) {
                            // Fix: Use the standard getEntities call that takes a player and AABB
                            if (mc.level.getEntities((Entity)mc.player, new AABB(crystalPos)).isEmpty()) {
                                return pos; 
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private int findItemInHotbar(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }
}
