package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class AirJump extends Module {

    public AirJump() {
        super("AirJump", "Allows jumping while airborne.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        if (mc.options.keyJump.isDown()) {
            
            if (!mc.player.onGround()) {
                mc.player.jumpFromGround();
            }
        }
    }
}
