package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting; // Don't forget this import!

public class AirJump extends Module {

    // Add a new Setting of type Boolean (bool is likely a helper method in Module/Feature)
    private final Setting<Boolean> checkGround = bool("CheckGround", true);

    public AirJump() {
        super("AirJump", "Allows jumping while airborne.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // Use the new setting to control the module's logic
        if (checkGround.getValue() && mc.player.onGround()) {
            return; // If the setting is true and we're on the ground, do nothing
        }

        if (mc.options.keyJump.isDown()) {
            // This is the core logic that executes when the key is pressed and the module is active
            mc.player.jumpFromGround();
        }
    }
}
