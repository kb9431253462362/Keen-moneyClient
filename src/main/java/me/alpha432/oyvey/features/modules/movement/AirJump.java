package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.phys.Vec3; // You will likely need this import

public class AirJump extends Module {
    
    // Original setting (can be kept or removed)
    private final Setting<Boolean> checkGround = bool("CheckGround", true); 
    private final Setting<Double> jumpHeight = num("JumpHeight", 0.42, 0.1, 1.0); // New setting for control

    public AirJump() {
        super("AirJump", "Allows jumping while airborne.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // Skip if the player is not pressing the jump key or is on the ground (optional check)
        if (!mc.options.keyJump.isDown()) {
            return;
        }
        
        // This check is the original jump prevention, which you might want to remove for pure air jump
        // if (checkGround.getValue() && mc.player.onGround()) {
        //    return;
        // }

        // --- CORE FIX: Force an upward velocity change ---

        if (!mc.player.onGround()) { // Only apply the jump if NOT on the ground
            // 1. Get the current velocity
            Vec3 currentVelocity = mc.player.getDeltaMovement();
            
            // 2. Set the Y-velocity (vertical) to the custom jump height
            mc.player.setDeltaMovement(
                currentVelocity.x, 
                jumpHeight.getValue(), // Use the setting for the jump height
                currentVelocity.z
            );
            
            // If the key is held, this will keep running, resulting in a type of 'fly'. 
            // For a single air jump, you would add an extra check or use an event handler 
            // for the key press instead of onTick.
        }
    }
}
