package me.alpha432.oyvey.features.modules.player; // Changed package assumption to player

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

public class AutoTool extends Module {

    public AutoTool() {
        super("AutoTool", "Automatically switches to the best tool.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        
        // NOTE: This logic requires utility to get the targeted block.
        // Assuming mc.gameMode.isDestroying() means a block is being targeted/broken.
        if (mc.gameMode.isDestroying()) {
            BlockPos targetPos = null; // Placeholder for targeted block position

            if (targetPos != null) {
                int bestSlot = findBestTool(targetPos);
                if (bestSlot != -1) {
                    // FIX: Use the setter method
                    mc.player.getInventory().setSelected(bestSlot); 
                }
            }
        }
    }
    
    private int findBestTool(BlockPos pos) {
        BlockState state = mc.level.getBlockState(pos);
        float bestSpeed = 1.0f;
        int bestSlot = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            float speed = stack.getDestroySpeed(state);

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        return bestSlot;
    }
}
