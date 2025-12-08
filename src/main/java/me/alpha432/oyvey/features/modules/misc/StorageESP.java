package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import java.awt.Color;

public class StorageESP extends Module {

    public StorageESP() {
        super("StorageESP", "Highlights storage blocks.", Category.RENDER);
    }


   @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;
        
        // FIX: Renamed blockEntitys to blockEntities
        for (BlockEntity blockEntity : mc.level.blockEntities) {
            Color color = null;

            if (blockEntity instanceof ChestBlockEntity) {
                color = new Color(255, 170, 0, 180); // Orange for Chest
            } else if (blockEntity instanceof EnderChestBlockEntity) {
                color = new Color(170, 0, 255, 180); // Purple for Ender Chest
            } else if (blockEntity instanceof HopperBlockEntity) {
                color = new Color(100, 100, 100, 180); // Gray for Hopper
            }
            // Add more block entity checks here (e.g., ShulkerBoxBlockEntity)

            if (color != null) {
                BlockPos pos = blockEntity.getBlockPos();
                // RenderUtil.drawBlockBox(pos, color); 
            }
        }
    }
}
