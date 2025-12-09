package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.phys.Vec3;

public class StrafeAssist extends Module {
    private final Setting<Float> speed = num("Speed", 1.2f, 0.5f, 2.0f);
    private final Setting<Boolean> autoJump = bool("AutoJump", false);

    public StrafeAssist() {
        super("StrafeAssist", "Helps with strafing movement.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (nullCheck() || mc.player.isInWater() || mc.player.isInLava()) return;
        
        double forward = mc.player.xxa;
        double strafe = mc.player.zza;
        if (forward == 0 && strafe == 0) return;

        float yaw = mc.player.getYRot();
        double motX = Math.cos(Math.toRadians(yaw + 90)) * strafe * speed.getValue() + Math.cos(Math.toRadians(yaw)) * forward * speed.getValue();
        double motZ = Math.sin(Math.toRadians(yaw + 90)) * strafe * speed.getValue() + Math.sin(Math.toRadians(yaw)) * forward * speed.getValue();
        
        mc.player.setDeltaMovement(motX / 20.0, mc.player.getDeltaMovement().y, motZ / 20.0);

        if (autoJump.getValue() && mc.player.onGround()) {
            mc.player.jumpFromGround();
        }
    }
}