package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EndCrystal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
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
        // Find the crystal closest to the player within breakRange
        Entity crystalToBreak = mc.level.getEntities()
            .stream()
            .filter(entity -> entity instanceof EndCrystal)
            .filter(entity -> mc.player.distanceTo(entity) <= breakRange.getValue())
            .min(Comparator.comparingDouble(mc.player::distanceTo))
            .orElse(null);

        if (crystalToBreak != null) {
            // Check for Anti-Weakness (if the client has the necessary code for switching tools)
            // if (antiWeakness.getValue() && InventoryUtil.isWeaknessApplied()) { InventoryUtil.switchToSword(); }
            
            mc.gameMode.attack(mc.player, crystalToBreak);
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private void doPlace() {
        // 1. Find a valid place position (Obsidian/Bedrock + 1 block up)
        BlockPos placePos = findObsidianPlacePos();

        if (placePos != null) {
            // 2. Find End Crystal in hotbar
            int crystalSlot = findItemInHotbar(Items.END_CRYSTAL);
            
            if (crystalSlot != -1) {
                // 3. Switch, Place, and Switch back
                int oldSlot = mc.player.getInventory().selected;
                
                // Switch to crystal slot
                mc.player.getInventory().selected = crystalSlot; 
                
                // Place the crystal
                mc.gameMode.useItemOn(
                    mc.player,
                    InteractionHand.MAIN_HAND,
                    // Use a dummy raycast hit to interact with the block
                    new net.minecraft.world.phys.BlockHitResult(
                        net.minecraft.world.phys.Vec3.atCenterOf(placePos),
                        net.minecraft.core.Direction.UP,
                        placePos,
                        false
                    )
                );

                // Switch back
                mc.player.getInventory().selected = oldSlot;
            }
        }
    }

    private BlockPos findObsidianPlacePos() {
        BlockPos playerPos = mc.player.blockPosition();

        // Simple iteration over a cube radius around the player
        for (int x = (int) -placeRange.getValue(); x <= placeRange.getValue(); x++) {
            for (int y = (int) -placeRange.getValue(); y <= placeRange.getValue(); y++) {
                for (int z = (int) -placeRange.getValue(); z <= placeRange.getValue(); z++) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    
                    // Check if the block is Obsidian or Bedrock
                    if (mc.level.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.level.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
                        BlockPos crystalPos = pos.above();
                        
                        // Check if the block above is air and the block 2 blocks above is air
                        if (mc.level.getBlockState(crystalPos).isAir() && mc.level.getBlockState(pos.above(2)).isAir()) {
                            // Check for entity collision (e.g., player or crystal already there)
                            if (mc.level.getEntities(mc.player, new AABB(crystalPos)).isEmpty()) {
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
