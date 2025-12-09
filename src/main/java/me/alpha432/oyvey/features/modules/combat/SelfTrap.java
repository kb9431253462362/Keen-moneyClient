package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SelfTrap extends Module {
    public Setting<Integer> delay = this.register(new Setting<>("Delay", 10, 0, 20));
    
    private int timer = 0;
    
    public SelfTrap() {
        super("SelfTrap", "Traps yourself with blocks", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        timer++;
        if (timer < delay.getValue()) return;
        timer = 0;
        
        BlockPos playerPos = mc.player.blockPosition();
        // Place blocks around and above
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                placeBlock(playerPos.offset(dx, 0, dz));
                placeBlock(playerPos.offset(dx, 1, dz));
            }
        }
        placeBlock(playerPos.above(2));
    }
    
    private void placeBlock(BlockPos pos) {
        if (mc.level == null || mc.player == null) return;
        if (!mc.level.getBlockState(pos).isCollisionShapeFullBlock(mc.level, pos)) return;
        
        BlockHitResult hit = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), 
            net.minecraft.core.Direction.UP, pos, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
    }
}
