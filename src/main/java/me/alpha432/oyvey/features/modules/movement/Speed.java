package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.phys.Vec3;

public class Speed extends Module {
    private final Setting<Double> speedMultiplier = num("SpeedMult", 1.5, 0.5, 5.0);
    private final Setting<Double> boostMultiplier = num("BoostMult", 2.5, 0.5, 5.0);
    private final Setting<Mode> mode = mode("Mode", Mode.Strafe);

    public Speed() {
        super("Speed", "Makes the player move faster. Boost with LShift.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (nullCheck() || mc.player.isInWater() || mc.player.isInLava()) return;

        double forward = mc.player.xxa;
        double strafe = mc.player.zza;
        float yaw = mc.player.getYRot();

        if (forward == 0.0 && strafe == 0.0) {
            mc.player.setDeltaMovement(0.0, mc.player.getDeltaMovement().y, 0.0);
            return;
        }

        double mult = mc.options.keyShift.isDown() ? boostMultiplier.getValue() : speedMultiplier.getValue();

        switch (mode.getValue()) {
            case Strafe -> applyStrafe(forward, strafe, yaw, mult);
            case Vanilla -> applyVanilla(mult);
        }
    }

    private void applyStrafe(double forward, double strafe, float yaw, double mult) {
        double yawRad = Math.toRadians(yaw);
        double motionX = (forward * Math.sin(yawRad) + strafe * Math.cos(yawRad)) * mult / 20.0;
        double motionZ = (forward * Math.cos(yawRad) - strafe * Math.sin(yawRad)) * mult / 20.0;
        mc.player.setDeltaMovement(new Vec3(motionX, mc.player.getDeltaMovement().y, motionZ));
    }

    private void applyVanilla(double mult) {
        Vec3 vel = mc.player.getDeltaMovement();
        mc.player.setDeltaMovement(vel.x * mult, vel.y, vel.z * mult);
    }

    public enum Mode {
        Strafe,
        Vanilla
    }
}
