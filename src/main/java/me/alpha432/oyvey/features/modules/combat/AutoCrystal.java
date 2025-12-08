package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EndCrystal;
import net.minecraft.world.entity.player.Player; // Import Player
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

    // --- Settings for Control ---
    private final Setting<Double> placeRange = num("PlaceRange", 5.0, 1.0, 6.0);
    private final Setting<Double> breakRange = num("BreakRange", 4.0, 1.0, 6.0);
    private final Setting<Double> targetRange = num("TargetRange", 12.0, 3.0, 20.0); // Range to find enemy player
    private final Setting<Boolean> antiWeakness = bool("AntiWeakness", true); // Keeping for future implementation
    private final Setting<Integer> tickDelay = num("TickDelay", 0, 0, 5); // Delay between actions (for speed control)

    // --- Internal State ---
    private int tickCounter = 0;
    private Player target = null;

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks end crystals.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // Implement a basic tick delay for speed control/server stability
        if (tickCounter < tickDelay.getValue()) {
            tickCounter++;
            return;
        }
        tickCounter = 0;

        // 1. Find the best enemy target
        target = findNearestTarget();
        if (target == null) return;

        // 2. Break existing crystals (High Priority)
        doBreak();

        // 3. Place new crystals
        doPlace();
    }

    private Player findNearestTarget() {
        // Find all living entities, filter for Players, exclude self, and check range
        List<Player> players = mc.level.players().stream()
            .filter(player -> !player.equals(mc.player)) // Exclude self
            .filter(player -> mc.player.distanceTo(player) <= targetRange.getValue())
            // FIX: You would add a friend check here if your client has one
            // .filter(player -> !OyVey.friendManager.isFriend(player.getName().getString())) 
            .sorted(Comparator.comparingDouble(mc.player::distanceTo)) // Sort by distance
            .collect(Collectors.toList());

        return players.isEmpty() ? null : players.get(0);
    }

    private void doBreak() {
        // Search in a box large enough to cover the breakRange
        AABB searchBox = mc.player.getBoundingBox().inflate(breakRange.getValue(), breakRange.getValue(), breakRange.getValue());
        
        Entity crystalToBreak = mc.level.getEntities(mc.player, searchBox).stream()
            .filter(entity -> entity instanceof EndCrystal)
            .filter(entity -> mc.player.distanceTo(entity) <= breakRange.getValue())
            .min(Comparator.comparingDouble(mc.player::distanceTo))
            .orElse(null);

        if (crystalToBreak != null) {
            // AntiWeakness logic would typically go here (e.g., switch to a tool/sword before attacking)

            mc.gameMode.attack(mc.player, crystalToBreak);
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private void doPlace() {
        // Only place if we have a target
        if (target == null) return;
        
        BlockPos placePos = findObsidianPlacePos();

        if (placePos != null) {
            int crystalSlot = findItemInHotbar(Items.END_CRYSTAL);
            
            if (crystalSlot != -1) {
                int oldSlot = mc.player.getInventory().selected;
                
                // Switch slot
                mc.player.connection.send(new ServerboundSetCarriedItemPacket(crystalSlot));
                mc.player.getInventory().selected = crystalSlot; 
                
                // Place crystal
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
                
                // Revert slot
                mc.player.connection.send(new ServerboundSetCarriedItemPacket(oldSlot));
                mc.player.getInventory().selected = oldSlot;
            }
        }
    }

    /**
     * Finds the best placeable Obsidian block to maximize damage on the target.
     * In a real CA, you would calculate explosion damage here. For simplicity,
     * this version just looks for the spot closest to the target.
     */
    private BlockPos findObsidianPlacePos() {
        BlockPos bestPos = null;
        double shortestDistanceToTarget = Double.MAX_VALUE;

        // The search should be centered around the target for best results, 
        // but limited by the player's placeRange.
        int range = placeRange.getValue().intValue();

        // Iterate over a box centered on the player
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = mc.player.blockPosition().offset(x, y, z);
                    
                    // 1. Check if the block is obsidian or bedrock
                    if (mc.level.getBlockState(pos).is(Blocks.OBSIDIAN) || mc.level.getBlockState(pos).is(Blocks.BEDROCK)) {
                        BlockPos crystalPos = pos.above();
                        
                        // 2. Check if the placement spot is clear (1 or 2 high)
                        if (mc.level.getBlockState(crystalPos).isAir() && mc.level.getBlockState(pos.above(2)).isAir()) {
                            
                            // 3. Check if no entities are blocking the placement spot
                            if (mc.level.getEntities(mc.player, new AABB(crystalPos)).isEmpty()) {
                                
                                // 4. Check if the placement is within range of the target
                                // This is a simple distance check; a real CA would calculate damage
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
