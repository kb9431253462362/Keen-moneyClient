package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
// Removed: import org.lwjgl.input.Keyboard; // This is the source of the error

public class AutoSprint extends Module {

    public AutoSprint() {
        // FIX: Removed the Keyboard.KEY_F argument which caused the error
        super("AutoSprint", "Automatically sprints when you walk forward.", Category.MOVEMENT);
        // You may need to set the keybind using a specific setter if your base supports it
        // Example: this.setKey(29); // If 29 is the keycode for 'F'
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // Check if the player is pressing the forward key (W)
        if (mc.options.keyMoveForward.isDown()) {
            
            // Check if the player is currently able to sprint
            if (canSprint()) {
                
                // Set the sprint flag
                mc.player.setSprinting(true);
            }
        }
    }
    
    private boolean canSprint() {
        // Checks if the player meets all standard Minecraft sprint requirements:
        return !mc.player.isSprinting()
               && !mc.player.isCrouching()
               && !mc.player.getAbilities().flying
               && !mc.player.isPassenger()
               && mc.player.getFoodData().getFoodLevel() > 6
               && mc.player.getVehicle() == null;
    }

    // Removed onEnable/onDisable as onTick handles the constant check and setting.
}
