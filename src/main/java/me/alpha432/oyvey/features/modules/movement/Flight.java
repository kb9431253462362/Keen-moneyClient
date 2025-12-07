package me.alpha432.oyvey.features.modules.movement; // Adjust package to fit the Player category if needed

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.entity.player.Abilities;

// Note: You would need to replace 'mc' with the correct client instance access 
// for the framework you are using (e.g., mc.player or client.player).

public class Flight extends Module {

    // You can add a Setting<Float> speed setting here if desired
    private final float FLY_SPEED = 0.05f; 

    public Flight() {
        super("AbilitiesFly", "Uses vanilla flight mechanics.", Category.MOVEMENT); 
    }

    @Override
    public void onEnable() {
        // When activated, grant the flight ability and set speed.
        if (mc.player != null) {
            Abilities abilities = mc.player.getAbilities();
            
            // Check if player is already in creative, if so, don't interfere
            if (abilities.creativeMode || mc.player.isSpectator()) return; 

            abilities.mayfly = true;          // Allow flying
            abilities.flying = true;          // Start flying immediately
            abilities.setFlySpeed(FLY_SPEED); // Set the speed
            mc.player.onUpdateAbilities();     // Crucial: tell the server about the change
        }
    }

    @Override
    public void onDisable() {
        // When deactivated, remove the flight ability.
        if (mc.player != null) {
            Abilities abilities = mc.player.getAbilities();
            
            // Revert only if the player wasn't in creative/spectator mode to begin with
            if (!abilities.creativeMode && !mc.player.isSpectator()) { 
                abilities.mayfly = false;
                abilities.flying = false;
                abilities.setFlySpeed(0.05f); // Reset to default walk speed
            }
            mc.player.onUpdateAbilities(); // Crucial: tell the server about the change
        }
    }

    @Override
    public void onTick() {
        // The reinforcement loop (similar to the logic in the Meteor client's onPostTick method)
        if (mc.player != null && !mc.player.getAbilities().creativeMode && !mc.player.isSpectator()) {
            
            // Reinforce flight ability and set speed every tick
            mc.player.getAbilities().mayfly = true;
            mc.player.getAbilities().setFlySpeed(FLY_SPEED);
            
            // Note: Constantly calling onUpdateAbilities() in onTick() is generally 
            // avoided in favor of the onEnable call to reduce server/network spam.
        }
    }
}
