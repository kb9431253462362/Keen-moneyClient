package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class Jesus extends Module {

    public Jesus() {
        super("Jesus", "Walk on water like the son of God.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // Check if the player is over a fluid block
        if (mc.player.isInWater() || mc.player.isInLava()) return;
        
        if (mc.player.isFallFlying()) return;

        // Check the block below the player
        if (mc.level.getFluidState(mc.player.blockPosition().below()).isSource()) {
            
            // Check if the player is falling or moving horizontally
            if (mc.player.getDeltaMovement().y < 0.0) {
                
                // Set the player's Y position slightly above the fluid's surface
                mc.player.setPos(mc.player.getX(), mc.player.getY() + 0.02, mc.player.getZ());
                
                // Force velocity to zero to stop falling
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.0, mc.player.getDeltaMovement().z);
                
                // This client-side code will make the player appear to stand on the surface.
                // A reliable Jesus module usually involves a Mixin to modify collision checks.
            }
        }
    }
}
