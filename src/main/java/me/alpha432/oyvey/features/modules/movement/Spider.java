package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class Spider extends Module {

    private final double CLIMB_SPEED = 0.2;

    public Spider() {
        super("Spider", "Climbs up walls like a spider.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        if (mc.player.horizontalCollision) {
            
            if (mc.options.keyJump.isDown() || mc.player.getDeltaMovement().y < CLIMB_SPEED) {
                
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, CLIMB_SPEED, mc.player.getDeltaMovement().z);
            }
        }
    }
}
