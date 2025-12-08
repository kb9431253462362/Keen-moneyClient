package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.RenderUtil; // Assumed client utility
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import java.awt.Color;
import java.util.Set;

public class ESP extends Module {

    // --- General Settings ---

    public final Setting<Mode> mode = register(enumSetting(Mode.class).name("Mode").defaultValue(Mode.Box).build());
    public final Setting<Boolean> highlightTarget = bool("Highlight Target", true);
    public final Setting<Boolean> targetHitbox = bool("Target Hitbox", true).visible(highlightTarget::getValue);
    public final Setting<Integer> outlineWidth = num("Outline Width", 2, 1, 10).visible(() -> mode.getValue() == Mode.Shader);
    public final Setting<Double> glowMultiplier = num("Glow Multiplier", 3.5, 0.0, 10.0).visible(() -> mode.getValue() == Mode.Shader);
    public final Setting<Boolean> ignoreSelf = bool("Ignore Self", true);
    public final Setting<ShapeMode> shapeMode = register(enumSetting(ShapeMode.class).name("Shape Mode").defaultValue(ShapeMode.Both).build()).visible(() -> mode.getValue() != Mode.Glow);
    public final Setting<Double> fillOpacity = num("Fill Opacity", 0.3, 0.0, 1.0).visible(() -> shapeMode.getValue() != ShapeMode.Lines && mode.getValue() != Mode.Glow);
    private final Setting<Double> fadeDistance = num("Fade Distance", 3.0, 0.0, 12.0);
    
    // NOTE: EntityTypeListSetting is replaced with a single boolean filter for simplicity
    private final Setting<Boolean> filterPlayers = bool("Players", true);

    // --- Color Settings ---

    public final Setting<Boolean> distance = bool("Distance Colors", false);
    public final Setting<Boolean> friendOverride = bool("Show Friend Colors", true).visible(distance::getValue);

    // These use Color type, assuming your client's color settings are wrappers around java.awt.Color
    private final Setting<Color> playersColor = color("Players Color", new Color(255, 255, 255, 255)).visible(() -> !distance.getValue());
    private final Setting<Color> animalsColor = color("Animals Color", new Color(25, 255, 25, 255)).visible(() -> !distance.getValue());
    private final Setting<Color> monstersColor = color("Monsters Color", new Color(255, 25, 25, 255)).visible(() -> !distance.getValue());
    private final Setting<Color> miscColor = color("Misc Color", new Color(175, 175, 175, 255)).visible(() -> !distance.getValue());
    private final Setting<Color> targetColor = color("Target Color", new Color(200, 200, 200, 255)).visible(highlightTarget::getValue);
    private final Setting<Color> targetHitboxColor = color("Target Hitbox Color", new Color(100, 200, 200, 255)).visible(() -> highlightTarget.getValue() && targetHitbox.getValue());

    private final Color lineColor = new Color(0, 0, 0, 0);
    private final Color sideColor = new Color(0, 0, 0, 0);
    private final Color baseColor = new Color(0, 0, 0, 0);

    private int count;

    public ESP() {
        super("ESP", "Renders entities through walls.", Category.RENDER);
    }

    // --- 3D Rendering (Box & Wireframe) ---

    @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck() || mode.getValue() == Mode._2D) return;

        count = 0;
        
        Entity target = null;
        if (highlightTarget.getValue() && targetHitbox.getValue() && mc.hitResult instanceof EntityHitResult hr) target = hr.getEntity();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (target != entity && shouldSkip(entity)) continue;
            
            if (target == entity || mode.getValue() == Mode.Box || mode.getValue() == Mode.Wireframe) {
                drawBoundingBox(event, entity);
            }
            count++;
        }
    }

    private void drawBoundingBox(Render3DEvent event, Entity entity) {
        Color color = getColor(entity);
        if (color == null) return;
        
        lineColor.set(color);
        sideColor.set(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * fillOpacity.getValue()));

        // NOTE: Wireframe mode requires a custom WireframeEntityRenderer utility (not included)
        if (mode.getValue() == Mode.Wireframe) {
            // WireframeEntityRenderer.render(event, entity, 1, sideColor, lineColor, shapeMode.getValue());
            return;
        }

        boolean isTarget = drawAsTarget(entity);

        if (mode.getValue() == Mode.Box || (targetHitbox.getValue() && isTarget)) {
            // Get interpolated position for smooth rendering
            double x = Mth.lerp(event.getPartialTicks(), entity.xOld, entity.getX()) - mc.getEntityRenderDispatcher().camera.getPosition().x;
            double y = Mth.lerp(event.getPartialTicks(), entity.yOld, entity.getY()) - mc.getEntityRenderDispatcher().camera.getPosition().y;
            double z = Mth.lerp(event.getPartialTicks(), entity.zOld, entity.getZ()) - mc.getEntityRenderDispatcher().camera.getPosition().z;

            ShapeMode shape = shapeMode.getValue();
            
            if (isTarget) {
                // Override colors and shape for target hitbox rendering
                lineColor.set(targetHitboxColor.getValue());
                sideColor.set(targetHitboxColor.getValue().getRed(), targetHitboxColor.getValue().getGreen(), targetHitboxColor.getValue().getBlue(), (int) (targetHitboxColor.getValue().getAlpha() * fillOpacity.getValue()));
                if (mode.getValue() != Mode.Box) shape = ShapeMode.Lines;
            }

            AABB box = entity.getBoundingBox().move(x, y, z);
            
            // Use your client's RenderUtil to draw the box
            RenderUtil.drawBox(
                box,
                lineColor,
                sideColor,
                shape == ShapeMode.Lines || shape == ShapeMode.Both, 
                shape == ShapeMode.Sides || shape == ShapeMode.Both
            );
        }
    }
    
    // --- 2D Rendering (Box) ---

    @Override
    public void onRender2D(Render2DEvent event) {
        if (nullCheck() || mode.getValue() != Mode._2D) return;
        
        // NOTE: Full 2D rendering requires screen-space projection logic (NametagUtils.to2D)
        // that is client-specific and too complex to fully adapt here.
        // We will skip the 2D render loop, as it requires extensive external utilities.
    }

    // --- Utils ---

    public boolean drawAsTarget(Entity entity) {
        return highlightTarget.getValue() && mc.hitResult instanceof EntityHitResult hr && hr.getEntity() == entity;
    }

    public boolean shouldSkip(Entity entity) {
        if (drawAsTarget(entity)) return false;
        if (entity == mc.player && ignoreSelf.getValue()) return true;
        if (entity == mc.cameraEntity && mc.options.getCameraType().isFirstPerson()) return true;
        
        // Simplified entity filtering: only check players for now
        if (entity instanceof Player && !filterPlayers.getValue()) return true;
        
        return !mc.level.entitiesForRendering().contains(entity);
    }

    public Color getColor(Entity entity) {
        Color color;
        double alpha = 1;

        if (drawAsTarget(entity)) {
            color = targetColor.getValue();
        } else {
            if (entity instanceof Player && !filterPlayers.getValue()) return null;

            alpha = getFadeAlpha(entity);
            if (alpha == 0) return null;

            color = getEntityTypeColor(entity);
        }

        // Apply fade alpha to the final color
        return baseColor.set(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * alpha));
    }

    private double getFadeAlpha(Entity entity) {
        // Calculate squared distance from player/camera to entity center
        double distSq = mc.player.distanceToSqr(entity);
        double fadeDistSq = fadeDistance.getValue() * fadeDistance.getValue();
        double alpha = 1;

        if (distSq <= fadeDistSq) {
            alpha = Math.sqrt(distSq) / fadeDistance.getValue();
        }
        if (alpha <= 0.075) alpha = 0;
        return alpha;
    }

    public Color getEntityTypeColor(Entity entity) {
        // NOTE: Distance coloring and friend system utilities are replaced with placeholders
        if (entity instanceof Player) {
            // Assume PlayerUtils/Friends utility check is here
            // Simplified: return friend color if friend and override is true, else distance color
            return playersColor.getValue();
        } else {
            return switch (entity.getType().getCategory()) {
                case CREATURE -> animalsColor.getValue();
                case WATER_AMBIENT, WATER_CREATURE -> animalsColor.getValue();
                case MONSTER -> monstersColor.getValue();
                default -> miscColor.getValue();
            };
        }
    }

    @Override
    public String getInfoString() {
        return Integer.toString(count);
    }
    
    // --- Enums ---

    public enum Mode {
        Box,
        Wireframe,
        _2D,
        Shader,
        Glow;

        @Override
        public String toString() {
            return this == _2D ? "2D" : super.toString();
        }
    }
    
    public enum ShapeMode {
        Lines,
        Sides,
        Both
    }
}
