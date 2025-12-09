package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BlockESP extends Module {
    public Setting<Color> color = color("Color", 255, 255, 0, 255);
    public Setting<Float> lineWidth = num("LineWidth", 1.0f, 0.1f, 5.0f);
    public Setting<Integer> range = num("Range", 64, 10, 256);

    private final Set<String> targetBlocks = new HashSet<>(); 

    public BlockESP() {
        super("BlockESP", "Highlights specific blocks in the world", Category.RENDER);
       targetBlocks.add("diamond_ore"); 
        targetBlocks.add("deepslate_diamond_ore"); 
        targetBlocks.add("emerald_ore");
        targetBlocks.add("deepslate_emerald_ore");
        targetBlocks.add("gold_ore");
        targetBlocks.add("deepslate_gold_ore");
        targetBlocks.add("nether_gold_ore");
        targetBlocks.add("iron_ore");
        targetBlocks.add("deepslate_iron_ore");
        targetBlocks.add("lapis_ore");
        targetBlocks.add("deepslate_lapis_ore");
        targetBlocks.add("redstone_ore");
        targetBlocks.add("deepslate_redstone_ore");
        targetBlocks.add("coal_ore");
        targetBlocks.add("deepslate_coal_ore");
        targetBlocks.add("copper_ore");
        targetBlocks.add("deepslate_copper_ore");
        targetBlocks.add("ancient_debris"); // Netherite ore
        
        // --- Storage and Rare Structures ---
        targetBlocks.add("chest");
        targetBlocks.add("ender_chest");
        targetBlocks.add("trapped_chest");
        targetBlocks.add("barrel");
        targetBlocks.add("shulker_box");
        targetBlocks.add("white_shulker_box");
        targetBlocks.add("orange_shulker_box");
        targetBlocks.add("magenta_shulker_box");
        targetBlocks.add("light_blue_shulker_box");
        targetBlocks.add("yellow_shulker_box");
        targetBlocks.add("lime_shulker_box");
        targetBlocks.add("pink_shulker_box");
        targetBlocks.add("gray_shulker_box");
        targetBlocks.add("light_gray_shulker_box");
        targetBlocks.add("cyan_shulker_box");
        targetBlocks.add("purple_shulker_box");
        targetBlocks.add("blue_shulker_box");
        targetBlocks.add("brown_shulker_box");
        targetBlocks.add("green_shulker_box");
        targetBlocks.add("red_shulker_box");
        targetBlocks.add("black_shulker_box");
        targetBlocks.add("furnace");
        targetBlocks.add("blast_furnace");
        targetBlocks.add("smoker");
        
        // --- Spawners and Utility Blocks ---
        targetBlocks.add("spawner");
        targetBlocks.add("beacon");
        targetBlocks.add("glowing_obsidian"); // Found in older clients, mostly used for rendering hacks
        targetBlocks.add("nether_portal");
    }

    private void addTargetBlock(String blockName) {
        targetBlocks.add(blockName.toLowerCase());
    }

    private void removeTargetBlock(String blockName) {
        targetBlocks.remove(blockName.toLowerCase());
    }

    public Set<String> getTargetBlocks() {
        return targetBlocks;
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
        int minY = playerY - 32;
        int maxY = playerY + 32;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y < maxY; y++) { // Note: Use < maxY instead of <= maxY for build limits
                    
                    BlockPos currentPos = new BlockPos(x, y, z);
                    
                    if (!mc.level.isLoaded(currentPos)) continue; 

                    Block block = mc.level.getBlockState(currentPos).getBlock();
                    ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
                    
                    if (targetBlocks.contains(blockId.getPath())) { 
                        VoxelShape shape = mc.level.getBlockState(currentPos).getShape(mc.level, currentPos);
                        if (shape.isEmpty()) continue;
                        
                        AABB box = shape.bounds().move(currentPos);
                        
                        RenderUtil.drawBox(
                            event.getMatrix(), 
                            box, 
                            color.getValue(), 
                            lineWidth.getValue()
                        );
                    }
                }
            }
        }
    }
}