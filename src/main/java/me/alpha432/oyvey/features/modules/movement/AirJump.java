package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.phys.Vec3; // Required for handling player velocity

public class AirJump extends Module {

    // ⚙️ GUI Setting: Allows the user to configure the jump power.
    private final Setting<Double> jumpHeight = num("JumpHeight", 0.42, 0.1, 1.0);
    
    // ⚙️ GUI Setting: Allows the user to toggle an optional check for ground.
    private final Setting<Boolean> preventGroundJump = bool("PreventGroundJump", true);


    public AirJump() {
        // Module Name, Description, and Category are defined here.
        super("AirJump", "Allows jumping repeatedly while airborne.", Category.MOVEMENT);
    }

    // This module does not require onEnable or onDisable cleanup.

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // 1. Check if the player is pressing the jump key (Spacebar)
        if (!mc.options.keyJump.isDown()) {
            return;
        }

        // 2. Check if we should prevent jumping while on the ground (optional setting)
        if (preventGroundJump.getValue() && mc.player.onGround()) {
             return;
        }
        
        // 3. CORE LOGIC: Apply the air jump if the player is NOT on the ground.
        if (!mc.player.onGround()) {
            
            // Get the current horizontal velocity to maintain momentum
            Vec3 currentVelocity = mc.player.getDeltaMovement();
            
            // Apply new velocity: keep X and Z, set Y to the value of the 'JumpHeight' setting.
            // This bypasses Minecraft's internal ground check for jumping.
            mc.player.setDeltaMovement(
                currentVelocity.x, 
                jumpHeight.getValue(), 
                currentVelocity.z
            );
        }
    }
}
