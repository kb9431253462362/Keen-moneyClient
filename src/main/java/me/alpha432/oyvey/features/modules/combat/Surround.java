package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class Surround extends Module {

    public Surround() {
        super("Surround", "Places Obsidian around and above your feet.", Category.COMBAT);
    }

    private int lastSlot = -1;

    @Override
    public void onTick() {
        if (nullCheck()) return;

        int obsidianSlot = findItemInHotbar(Items.OBSIDIAN);

        if (obsidianSlot == -1) {
            sendMessage("Obsidian not found in hotbar! Disabling.");
            toggle();
            return;
        }

        // 1. Get the player's current block position (feet level)
        BlockPos playerPos = mc.player.getOnPos();
        
        // 2. Define all necessary positions
        BlockPos[] surroundPositions = new BlockPos[] {
            playerPos.north(), 
            playerPos.south(), 
            playerPos.east(), 
            playerPos.west(),
            
            // Pillar above head (optional, but requested for full enclosure)
            playerPos.above(2)
        };

        // 3. Place blocks
        for (BlockPos pos : surroundPositions) {
            if (mc.level.getBlockState(pos).isAir() && mc.player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 25) {
                
                // Only place one block per tick for smoother execution
                placeBlock(pos, obsidianSlot);
                return; 
            }
        }
    }
    
    // --- Helper methods for placement and inventory ---
    
    private void placeBlock(BlockPos pos, int slot) {
        // Save the current slot to switch back later
        if (lastSlot == -1) {
            lastSlot = mc.player.getInventory().selected;
        }
        
        // Switch to Obsidian
        mc.player.getInventory().selected = slot;
        
        // Use the game mode to place the block
        // We assume we can always place against the block below the target
        mc.gameMode.useItemOn(
            mc.player,
            InteractionHand.MAIN_HAND,
            new BlockHitResult(
                Vec3.atCenterOf(pos),
                Direction.DOWN, // Assuming we are interacting with the block below the empty space
                pos.below(),
                false
            )
        );
        
        mc.player.swing(InteractionHand.MAIN_HAND);
        
        // Reset lastSlot to ensure we only switch back once the job is done or on disable
        lastSlot = -2; // Use -2 as a signal that a block was placed
    }

    private int findItemInHotbar(net.minecraft.world.item.Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void onDisable() {
        // Switch back to the original slot when the module is disabled
        if (lastSlot != -1 && lastSlot != -2) {
             mc.player.getInventory().selected = lastSlot;
        }
        lastSlot = -1;
    }
}
