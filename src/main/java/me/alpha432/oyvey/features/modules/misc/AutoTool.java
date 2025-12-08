package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.tags.ItemTags;

public class AutoTool extends Module {

    private BlockPos currentBlock = null;
    
    public AutoTool() {
        super("AutoTool", "Automatically switches to the most effective tool.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // Check if the player is currently targeting and breaking a block
        if (mc.gameMode.isDestroying() && mc.hitResult != null && mc.hitResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            BlockPos breakingBlock = mc.hitResult.getBlockPos();
            
            if (currentBlock == null || !currentBlock.equals(breakingBlock)) {
                currentBlock = breakingBlock;
                findAndSwitchTool(breakingBlock);
            }
        } else {
            currentBlock = null;
        }
    }

    private void findAndSwitchTool(BlockPos pos) {
        BlockState blockState = mc.level.getBlockState(pos);
        ItemStack currentStack = mc.player.getMainHandStack();

        // 1. Calculate the base score of the currently held item.
        double bestScore = getScore(currentStack, blockState);
        int bestSlot = -1;

        // 2. Iterate through the hotbar slots (0-8)
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.getInventory().getItem(i);

            double score = getScore(itemStack, blockState);

            // 3. Find the tool with the highest score
            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }
        
        // 4. Switch if a better tool was found
        if (bestSlot != -1 && bestSlot != mc.player.getInventory().selected) {
            mc.player.getInventory().selected = bestSlot;
        }
    }
    
    public static double getScore(ItemStack itemStack, BlockState state) {
        // Base checks: return -1 if the item is not suitable for the block
        if (!itemStack.isCorrectToolForDrops(state) && !(itemStack.getItem() instanceof ShearsItem && state.is(net.minecraft.tags.BlockTags.LEAVES)))
            return -1;

        double score = 0;

        // Mining Speed (Highest Weight)
        score += itemStack.getDestroySpeed(state) * 1000.0; 
        
        // Enchantment Scores (Efficiency, Unbreaking, Mending)
        score += getEnchantmentLevel(itemStack, Enchantments.EFFICIENCY);
        score += getEnchantmentLevel(itemStack, Enchantments.UNBREAKING);
        score += getEnchantmentLevel(itemStack, Enchantments.MENDING);
        score += getEnchantmentLevel(itemStack, Enchantments.FORTUNE);
        score += getEnchantmentLevel(itemStack, Enchantments.SILK_TOUCH);

        return score;
    }

    public static int getEnchantmentLevel(ItemStack stack, net.minecraft.world.item.enchantment.Enchantment enchantment) {
        // Placeholder for a proper utility method to get enchantment level.
        // Assumes your ItemStack implementation has this method or component lookup.
        return stack.getEnchantmentLevel(enchantment);
    }
}
