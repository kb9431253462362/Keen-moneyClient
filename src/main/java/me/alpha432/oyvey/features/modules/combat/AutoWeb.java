package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AutoWeb extends Module {
    public Setting<Double> range = this.register(new Setting<>("Range", 6.0, 0.0, 12.0));
    public AutoWeb() {
        super("AutoWeb", "Automatically places webs on enemies", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        mc.level.players().forEach(entity -> {
            if (entity.equals(mc.player)) return;
            if (entity.distanceTo(mc.player) > range.getValue()) return;
            
            BlockPos pos = entity.blockPosition();
            placeBlock(pos);
            placeBlock(pos.above());
        });
    }
    
    private void placeBlock(BlockPos pos) {
        if (mc.level == null || mc.player == null) return;
        if (!mc.level.getBlockState(pos).isCollisionShapeFullBlock(mc.level, pos)) return;
        
        BlockHitResult hit = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), 
            net.minecraft.core.Direction.UP, pos, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
    }
}
