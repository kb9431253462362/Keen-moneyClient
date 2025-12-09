package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SelfWeb extends Module {
    public Setting<Boolean> autoPlace = this.register(new Setting<>("AutoPlace", true));
    public SelfWeb() {
        super("SelfWeb", "Automatically places web around yourself", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null || !autoPlace.getValue()) return;
        
        BlockPos playerPos = mc.player.blockPosition();
        
        // Place web in cardinal directions
        placeBlock(playerPos.north());
        placeBlock(playerPos.south());
        placeBlock(playerPos.east());
        placeBlock(playerPos.west());
        placeBlock(playerPos.above());
    }
    
    private void placeBlock(BlockPos pos) {
        if (mc.level == null || mc.player == null) return;
        if (!mc.level.getBlockState(pos).isCollisionShapeFullBlock(mc.level, pos)) return;
        
        BlockHitResult hit = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), 
            net.minecraft.core.Direction.UP, pos, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
    }
}
