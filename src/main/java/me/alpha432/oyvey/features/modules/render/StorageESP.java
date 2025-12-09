package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class StorageESP extends Module {
    public Setting<Double> range = this.register(new Setting<>("Range", 30.0, 0.0, 100.0));
    
    public StorageESP() {
        super("StorageESP", "Highlights chests and storage", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null || mc.player == null) return;
        
        // Scan for storage blocks in range
        int rangeInt = (int) Math.ceil(range.getValue());
        BlockPos playerPos = mc.player.blockPosition();
        
        for (int x = playerPos.getX() - rangeInt; x <= playerPos.getX() + rangeInt; x++) {
            for (int y = playerPos.getY() - rangeInt; y <= playerPos.getY() + rangeInt; y++) {
                for (int z = playerPos.getZ() - rangeInt; z <= playerPos.getZ() + rangeInt; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    var block = mc.level.getBlockState(pos).getBlock();
                    
                    // Check if it's a storage block
                    if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || 
                        block == Blocks.FURNACE || block == Blocks.BARREL) {
                        // Would be highlighted in render event
                    }
                }
            }
        }
    }
}
