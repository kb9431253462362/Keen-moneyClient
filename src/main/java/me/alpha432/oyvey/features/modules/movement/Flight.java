package me.alpha432.oyvey.features.modules.movement; 

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.entity.player.Abilities;

public class Flight extends Module {

    // You can adjust this value to change the flight speed. Default is 0.05f.
    private final float DEFAULT_FLIGHT_SPEED = 0.05f; 

    public Flight() {
        // Renamed to "Flight" as requested.
        super("Flight", "Uses vanilla flight mechanics, reinforced every tick.", Category.MOVEMENT); 
    }

    // --- Activation Logic (onEnable) ---
    
    @Override
    public void onEnable() {
        if (mc.player == null) return;
        
        Abilities abilities = mc.player.getAbilities();
        
        // Use 'instabuild' (Creative Mode) check which resolved your previous error
        if (abilities.instabuild || mc.player.isSpectator()) return; 

        abilities.mayfly = true;          // Allow flying
        abilities.flying = true;          // Start flying immediately
        
        // Use 'setFlyingSpeed' which resolved your previous error
        abilities.setFlyingSpeed(DEFAULT_FLIGHT_SPEED); 
        
        mc.player.onUpdateAbilities();     // Update the server
    }

    // --- Deactivation Logic (onDisable) ---

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        
        Abilities abilities = mc.player.getAbilities();
        
        // Revert abilities only if the player wasn't already flying in Creative/Spectator
        if (!abilities.instabuild && !mc.player.isSpectator()) { 
            abilities.mayfly = false;
            abilities.flying = false;
            
            // Reset speed to default walk/flight speed
            abilities.setFlyingSpeed(DEFAULT_FLIGHT_SPEED); 
        }
        mc.player.onUpdateAbilities(); 
    }

    // --- Reinforcement Logic (onTick) ---

    @Override
    public void onTick() {
        // This loop ensures the ability remains active even if the server tries to turn it off.
        if (mc.player != null) {
            Abilities abilities = mc.player.getAbilities();
            
            // Reinforce if the module is active and the player isn't in a vanilla fly mode
            if (!abilities.instabuild && !mc.player.isSpectator()) {
                
                // Only set if the ability is not already active 
                if (!abilities.mayfly) {
                    abilities.mayfly = true;
                    abilities.setFlyingSpeed(DEFAULT_FLIGHT_SPEED); 
                }
            }
        }
    }
}
