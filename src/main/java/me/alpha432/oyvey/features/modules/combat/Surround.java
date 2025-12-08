package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;

public class Surround extends Module {
    
    public Surround() {
        super("Surround", "Places blocks around your feet.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // Check for obsidian (requires inventory utility methods)
        // if (!InventoryUtil.isHoldingItem(Items.OBSIDIAN)) return;

        BlockPos playerPos = mc.player.getOnPos();
        
        // Positions directly around the player's feet
        BlockPos[] surroundPositions = new BlockPos[] {
            playerPos.north(), playerPos.south(), playerPos.east(), playerPos.west()
        };
        
        for (BlockPos pos : surroundPositions) {
            if (mc.level.getBlockState(pos).isAir()) {
                // Logic to switch to obsidian and place block
                // BlockUtil.placeBlock(pos, EnumFacing.DOWN);
                return; // Place one block per tick
            }
        }
    }
}
