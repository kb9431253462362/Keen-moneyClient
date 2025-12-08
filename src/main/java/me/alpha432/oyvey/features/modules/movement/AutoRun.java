package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint", "Automatically sprints when you walk forward.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // 1. Check if the player is pressing the forward key (W)
        if (mc.options.keyForward.isDown()) {
            
            // 2. Check if the player is currently able to sprint
            if (canSprint()) {
                
                // 3. Set the sprint flag
                mc.player.setSprinting(true);
            }
        }
    }

    private boolean canSprint() {
        // Checks if the player meets all standard Minecraft sprint requirements:
        return !mc.player.isSSprinting()          // Player is not already trying to sprint (redundant but safe)
               && !mc.player.isCrouching()        // Not sneaking
               && !mc.player.getAbilities().flying // Not flying/spectator
               && !mc.player.isPassenger()        // Not riding an entity
               && mc.player.getFoodData().getFoodLevel() > 6 // Enough hunger
               && mc.player.getVehicle() == null; // No vehicle
    }
    
    // No need for onDisable logic here since we are not forcing the W key down.
}
