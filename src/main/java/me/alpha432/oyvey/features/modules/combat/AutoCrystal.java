package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
// FIX: Changed EndCrystal to EndCrystalEntity
import net.minecraft.world.entity.decoration.EndCrystalEntity; 
import net.minecraft.world.entity.player.Player; 
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket; 
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AutoCrystal extends Module {

    private final Setting<Double> placeRange = num("PlaceRange", 5.0, 1.0, 6.0);
    private final Setting<Double> breakRange = num("BreakRange", 4.0, 1.0, 6.0);
    private final Setting<Double> targetRange = num("TargetRange", 12.0, 3.0, 20.0);
    private final Setting<Boolean> antiWeakness = bool("AntiWeakness", true);
    private final Setting<Integer> tickDelay = num("TickDelay", 0, 0, 5); 

    private int tickCounter = 0;
    private Player target = null;

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks end crystals.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (tickCounter < tickDelay.getValue()) {
            tickCounter++;
            return;
        }
        tickCounter = 0;

        target = findNearestTarget();
        if (target == null) return;

        doBreak();
        doPlace();
    }

    private Player findNearestTarget() {
        List<Player> players = mc.level.players().stream()
            .filter(player -> !player.equals(mc.player))
            .filter(player -> mc.player.distanceTo(player) <= targetRange.getValue())
            // Add friends check here if available
            .sorted(Comparator.comparingDouble(mc.player::distanceTo))
            .collect(Collectors.toList());

        return players.isEmpty() ? null : players.get(0);
    }

    private void doBreak() {
        AABB searchBox = mc.player.getBoundingBox().inflate(breakRange.getValue(), breakRange.getValue(), breakRange.getValue());
        
        Entity crystalToBreak = mc.level.getEntities(mc.player, searchBox).stream()
            // FIX: Check for EndCrystalEntity
            .filter(entity -> entity instanceof EndCrystalEntity) 
            .filter(entity -> mc.player.distanceTo(entity) <= breakRange.getValue())
            .min(Comparator.comparingDouble(mc.player::distanceTo))
            .orElse(null);

        if (crystalToBreak != null) {
            mc.gameMode.attack(mc.player, crystalToBreak);
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private void doPlace() {
        if (target == null) return;
        
        BlockPos placePos = findObsidianPlacePos();

        if (placePos != null) {
            int crystalSlot = findItemInHotbar(Items.END_CRYSTAL);
            
            if (crystalSlot != -1) {
                int oldSlot = mc.player.getInventory().selected;
                
                // FIX: Use packet to switch slot (Fixes private access errors)
                mc.player.connection.send(new ServerboundSetCarriedItemPacket(crystalSlot));
                mc.player.getInventory().selected = crystalSlot; // Line 102/106/123 fixed
                
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
                mc.player.swing(InteractionHand.MAIN_HAND);
                
                mc.player.connection.send(new ServerboundSetCarriedItemPacket(oldSlot));
                mc.player.getInventory().selected = oldSlot;
            }
        }
    }

    private BlockPos findObsidianPlacePos() {
        BlockPos bestPos = null;
        double shortestDistanceToTarget = Double.MAX_VALUE;

        int range = placeRange.getValue().intValue();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = mc.player.blockPosition().offset(x, y, z);
                    
                    if (mc.level.getBlockState(pos).is(Blocks.OBSIDIAN) || mc.level.getBlockState(pos).is(Blocks.BEDROCK)) {
                        BlockPos crystalPos = pos.above();
                        
                        if (mc.level.getBlockState(crystalPos).isAir() && mc.level.getBlockState(pos.above(2)).isAir()) {
                            
                            if (mc.level.getEntities(mc.player, new AABB(crystalPos)).isEmpty()) {
                                
                                double distanceToTarget = target.distanceToSqr(Vec3.atCenterOf(crystalPos));
                                
                                if (distanceToTarget < shortestDistanceToTarget) {
                                    shortestDistanceToTarget = distanceToTarget;
                                    bestPos = pos;
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestPos;
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
