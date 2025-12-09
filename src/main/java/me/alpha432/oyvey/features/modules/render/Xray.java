package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Xray extends Module {
    public Setting<Color> color = color("Color", 255, 100, 0, 120);
    public Setting<Float> lineWidth = num("LineWidth", 1.0f, 0.1f, 5.0f);
    public Setting<Integer> range = num("Range", 64, 8, 256);

    private final Set<String> targetBlocks = new HashSet<>();

    public Xray() {
        super("Xray", "Highlights ores and important blocks", Category.RENDER);

        // default targets
        targetBlocks.add("diamond_ore");
        targetBlocks.add("deepslate_diamond_ore");
        targetBlocks.add("emerald_ore");
        targetBlocks.add("deepslate_emerald_ore");
        targetBlocks.add("gold_ore");
        targetBlocks.add("iron_ore");
        targetBlocks.add("lapis_ore");
        targetBlocks.add("redstone_ore");
        targetBlocks.add("coal_ore");
        targetBlocks.add("ancient_debris");
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.level == null) return;

        double renderRange = range.getValue();
        BlockPos playerPos = mc.player.blockPosition();

        int minX = (int) (playerPos.getX() - renderRange);
        int maxX = (int) (playerPos.getX() + renderRange);
        int minZ = (int) (playerPos.getZ() - renderRange);
        int maxZ = (int) (playerPos.getZ() + renderRange);
        int playerY = playerPos.getY();
        int minY = playerY - 16;
        int maxY = playerY + 16;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!mc.level.isLoaded(pos)) continue;
                    Block block = mc.level.getBlockState(pos).getBlock();
                    ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                    if (id == null) continue;
                    if (!targetBlocks.contains(id.getPath())) continue;

                    VoxelShape shape = mc.level.getBlockState(pos).getShape(mc.level, pos);
                    if (shape.isEmpty()) continue;

                    AABB box = shape.bounds().move(pos);
                    RenderUtil.drawBoxFilled(event.getMatrix(), box, color.getValue());
                    RenderUtil.drawBox(event.getMatrix(), box, color.getValue(), lineWidth.getValue());
                }
            }
        }
    }
}
