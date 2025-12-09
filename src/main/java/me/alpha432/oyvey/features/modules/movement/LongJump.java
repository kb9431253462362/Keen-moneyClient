package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.phys.Vec3;

public class LongJump extends Module {

    public Setting<Float> jumpHeight = num("Height", 0.5f, 0.1f, 1.0f);
    public Setting<Float> factor = num("Factor", 1.5f, 1.0f, 5.0f);

    public LongJump() {
        super("LongJump", "Performs a longer-than-usual jump.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (mc.options.keyJump.isDown() && mc.player.onGround()) {
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, jumpHeight.getValue(), mc.player.getDeltaMovement().z);
            
            double forward = mc.player.xxa;
            double strafe = mc.player.zza;
            float yaw = mc.player.getYRot();
            
            if (forward != 0.0 || strafe != 0.0) {
                double speed = factor.getValue() * 0.2873; 
                double motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
                double motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
                
                mc.player.setDeltaMovement(new Vec3(motionX, mc.player.getDeltaMovement().y, motionZ));
            }
        }
    }
}