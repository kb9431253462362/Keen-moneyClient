package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;

public class AimAssist extends Module {
    private final Setting<Double> range = num("Range", 6.0, 1.0, 12.0);
    private final Setting<Float> smooth = num("Smooth", 6.0f, 1.0f, 20.0f);
    private final Setting<Boolean> players = bool("Players", true);
    private final Setting<Boolean> mobs = bool("Mobs", false);

    public AimAssist() {
        super("AimAssist", "Smoothly adjusts your aim toward targets", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        AABB box = mc.player.getBoundingBox().inflate(range.getValue());
        LivingEntity target = (LivingEntity) mc.level.getEntities(mc.player, box, e ->
                e instanceof LivingEntity && !e.isSpectator() && !e.equals(mc.player) &&
                        ((players.getValue() && e instanceof net.minecraft.world.entity.player.Player) || (mobs.getValue() && e instanceof net.minecraft.world.entity.monster.Monster))
        ).stream().min(Comparator.comparingDouble(mc.player::distanceToSqr)).orElse(null);

        if (target == null) return;

        // Smoothly look at entity using RotationManager
        float[] angles = me.alpha432.oyvey.util.MathUtil.calcAngle(mc.player.getEyePosition(), target.getEyePosition());
        float targetYaw = angles[0];
        float targetPitch = angles[1];

        // interpolate
        float currentYaw = mc.player.getYRot();
        float currentPitch = mc.player.getXRot();

        float newYaw = currentYaw + (targetYaw - currentYaw) / smooth.getValue();
        float newPitch = currentPitch + (targetPitch - currentPitch) / smooth.getValue();

        OyVey.rotationManager.setPlayerRotations(newYaw, newPitch);
    }
}
