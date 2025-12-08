package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class SafeWalk extends Module {
    
    public SafeWalk() {
        super("SafeWalk", "Prevents you from walking off edges.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        
        Vec3 forward = mc.player.getForward();
        BlockPos nextBlockPos = mc.player.getOnPos().offset((int) forward.x, -1, (int) forward.z);
        
      if (level.isEmptyBlock(nextBlockPos) && mc.player.onGround()) {
             // FIX: keyForward changed to keyUp
             if (mc.options.keyUp.isDown()) {
                 // FIX: keyForward changed to keyUp
                 mc.options.keyUp.setDown(false);
             }
        }
}
