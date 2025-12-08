package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.RenderUtil; // Your client's renderer
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.enums.ChestType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class StorageESP extends Module {

  
    
    // Mode Settings
    private final Setting<Mode> mode = register(enumSetting(Mode.class).name("Mode").defaultValue(Mode.Box).build());
    private final Setting<ShapeMode> shapeMode = register(enumSetting(ShapeMode.class).name("Shape Mode").defaultValue(ShapeMode.Both).build());
    private final Setting<Integer> fillOpacity = num("Fill Opacity", 50, 0, 255);
    private final Setting<Double> fadeDistance = num("Fade Distance", 6.0, 0.0, 12.0);

    // Filter Settings (Simplified List<BlockEntityType<?>> to individual bools)
    private final Setting<Boolean> chests = bool("Chests", true);
    private final Setting<Boolean> enderChests = bool("Ender Chests", true);
    private final Setting<Boolean> shulkers = bool("Shulkers", true);
    private final Setting<Boolean> otherStorage = bool("Other Storage", true); // Furnaces, Hoppers, etc.
    
    // Color Settings (Using java.awt.Color, assuming internal conversion/handling)
    private final Setting<Color> colorChest = color("Chest Color", new Color(255, 160, 0, 255));
    private final Setting<Color> colorTrappedChest = color("Trapped Chest Color", new Color(255, 0, 0, 255));
    private final Setting<Color> colorBarrel = color("Barrel Color", new Color(255, 160, 0, 255));
    private final Setting<Color> colorShulker = color("Shulker Color", new Color(255, 160, 0, 255));
    private final Setting<Color> colorEnderChest = color("Ender Chest Color", new Color(120, 0, 255, 255));
    private final Setting<Color> colorOther = color("Other Color", new Color(140, 140, 140, 255));
    
    // Opened Block Tracking Settings
    private final Setting<Boolean> hideOpened = bool("Hide Opened", false);
    private final Setting<Color> openedColor = color("Opened Color", new Color(203, 90, 203, 0)); // Transparent default

    // Runtime state
    private final Set<BlockPos> interactedBlocks = new HashSet<>();
    private final Color renderLineColor = new Color(0, 0, 0, 0);
    private final Color renderSideColor = new Color(0, 0, 0, 0);

    public StorageESP() {
        super("StorageESP", "Renders all specified storage blocks.", Category.RENDER);
    }
    
    // --- Adapted Event Handlers ---
    
    @Override
    public void onEnable() {
        interactedBlocks.clear();
    }

    @Override
    public void onDisable() {
        interactedBlocks.clear();
    }

    // Since we don't have the Orbit @EventHandler system, we simulate the interaction event
    // by using a tick-based check or relying on client-specific event system if available.
    // For simplicity, we skip the InteractBlockEvent adaptation, as it's complex.
    // We will assume interactedBlocks are cleared via a manual button for now.

    @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;

        for (BlockEntity blockEntity : mc.level.blockEntityList) {
            
            // 1. Check if the block is filtered or opened
            boolean interacted = interactedBlocks.contains(blockEntity.getBlockPos());
            if (interacted && hideOpened.getValue()) continue;
            
            // 2. Determine base color and if it should render
            if (!getBlockEntityColor(blockEntity)) continue;

            // 3. Apply opened color if applicable
            if (interacted && openedColor.getValue().getAlpha() > 0) {
                renderLineColor.set(openedColor.getValue());
                renderSideColor.set(openedColor.getValue());
                renderSideColor.a = fillOpacity.getValue();
            }
            
            // 4. Calculate distance fade
            double distSq = mc.player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos()));
            double fadeDistSq = fadeDistance.getValue() * fadeDistance.getValue();
            double alphaMultiplier = 1.0;
            
            if (distSq <= fadeDistSq) {
                alphaMultiplier = distSq / fadeDistSq;
            }
            
            if (alphaMultiplier < 0.075) continue;

            // 5. Apply fade to colors
            Color finalLineColor = new Color(renderLineColor.getRed(), renderLineColor.getGreen(), renderLineColor.getBlue(), (int)(renderLineColor.getAlpha() * alphaMultiplier));
            Color finalSideColor = new Color(renderSideColor.getRed(), renderSideColor.getGreen(), renderSideColor.getBlue(), (int)(renderSideColor.getAlpha() * alphaMultiplier));

            // 6. Render based on mode
            if (mode.getValue() == Mode.Box) {
                renderBox(event, blockEntity, finalLineColor, finalSideColor);
            }
            
            // NOTE: Shader mode requires complex integration and is skipped here.
        }
    }
    
    // --- Core Logic ---
    
    private boolean getBlockEntityColor(BlockEntity blockEntity) {
        if (blockEntity instanceof TrappedChestBlockEntity) {
            if (!chests.getValue()) return false;
            renderLineColor.set(colorTrappedChest.getValue());
        }
        else if (blockEntity instanceof ChestBlockEntity) {
            if (!chests.getValue()) return false;
            renderLineColor.set(colorChest.getValue());
        }
        else if (blockEntity instanceof BarrelBlockEntity) {
            if (!chests.getValue()) return false;
            renderLineColor.set(colorBarrel.getValue());
        }
        else if (blockEntity instanceof ShulkerBoxBlockEntity) {
            if (!shulkers.getValue()) return false;
            renderLineColor.set(colorShulker.getValue());
        }
        else if (blockEntity instanceof EnderChestBlockEntity) {
            if (!enderChests.getValue()) return false;
            renderLineColor.set(colorEnderChest.getValue());
        }
        else if (blockEntity instanceof AbstractFurnaceBlockEntity || blockEntity instanceof BrewingStandBlockEntity || blockEntity instanceof CrafterBlockEntity || blockEntity instanceof DispenserBlockEntity || blockEntity instanceof HopperBlockEntity) {
            if (!otherStorage.getValue()) return false;
            renderLineColor.set(colorOther.getValue());
        }
        else {
            return false;
        }

        if (shapeMode.getValue() == ShapeMode.Sides || shapeMode.getValue() == ShapeMode.Both) {
            renderSideColor.set(renderLineColor);
            renderSideColor.a = fillOpacity.getValue();
        }
        return true;
    }

    private void renderBox(Render3DEvent event, BlockEntity blockEntity, Color lineColor, Color sideColor) {
        BlockPos pos = blockEntity.getBlockPos();
        double x1 = pos.getX();
        double y1 = pos.getY();
        double z1 = pos.getZ();
        double x2 = pos.getX() + 1;
        double y2 = pos.getY() + 1;
        double z2 = pos.getZ() + 1;
        
        // Basic double chest offset check (simplified)
        if (blockEntity instanceof ChestBlockEntity) {
            BlockState state = mc.level.getBlockState(pos);
            if (state.getBlock() == Blocks.CHEST && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
                // Adjust bounding box to not draw lines between joined chests
                // This is a complex step, simplified here by using standard box render.
            }
        }

        // Use your client's RenderUtil to draw the box
        RenderUtil.drawBox(
            pos, 
            x1, y1, z1, x2, y2, z2, 
            lineColor, 
            sideColor, 
            shapeMode.getValue() == ShapeMode.Lines, 
            shapeMode.getValue() == ShapeMode.Sides
        );
    }
    
    // --- Enums ---
    
    public enum Mode {
        Box,
        Shader
    }
    
    public enum ShapeMode {
        Lines,
        Sides,
        Both
    }
    
    // --- Custom Widget (for the "Clear Cache" button) ---
    
    // Since we don't have the Meteor GUI system, we skip this part.
    // If you need the button, you'll need to use your client's WButton equivalent.
}
