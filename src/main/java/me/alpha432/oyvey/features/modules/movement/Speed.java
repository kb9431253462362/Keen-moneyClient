package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.phys.Vec3;

public class Speed extends Module {

    // Base speed factor (0.2873 is vanilla speed in blocks/tick)
    private final double BASE_SPEED = 0.2873; 
    
    // Default factor when not holding Shift
    private final double DEFAULT_FACTOR = 1.3; 
    
    // Boost factor when holding Shift (3x faster than default)
    private final double BOOST_FACTOR = 3.9; // 1.3 * 3 = 3.9

    public Speed() {
        super("Speed", "Makes the player move faster. Boost with LShift.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.player.isInWater() || mc.player.isInLava()) return;

        double forward = mc.player.xxa;
        double strafe = mc.player.zza;
        float yaw = mc.player.getYRot();

        // 1. Determine the current speed multiplier
        double currentSpeedFactor;
        
        // Check if the sneak key (Left Shift) is currently pressed
        if (mc.options.keyShift.isDown()) {
            currentSpeedFactor = BOOST_FACTOR;
        } else {
            currentSpeedFactor = DEFAULT_FACTOR;
        }

        double finalSpeed = BASE_SPEED * currentSpeedFactor;

        if (forward == 0.0 && strafe == 0.0) {
            // Stop horizontal movement
            mc.player.setDeltaMovement(0.0, mc.player.getDeltaMovement().y, 0.0);
        } else {
            // 2. Calculate Direction and Set Velocity
            
            // Adjust speeds for horizontal movement calculation
            double motionX = (forward * finalSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * finalSpeed * Math.sin(Math.toRadians(yaw + 90.0f))) / 20.0;
            double motionZ = (forward * finalSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * finalSpeed * Math.cos(Math.toRadians(yaw + 90.0f))) / 20.0;
            
            // Apply the new horizontal velocity, preserving vertical velocity
            mc.player.setDeltaMovement(new Vec3(motionX, mc.player.getDeltaMovement().y, motionZ));
        }
    }
}
