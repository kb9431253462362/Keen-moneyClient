package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class Surround extends Module {
    public Setting<Boolean> doubleHeight = this.register(new Setting<>("DoubleHeight", false));
    public Surround() {
        super("Surround", "Surrounds you with protective blocks", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        BlockPos playerPos = mc.player.blockPosition();
        
        // Place blocks in a square around player
        placeBlock(playerPos.north());
        placeBlock(playerPos.south());
        placeBlock(playerPos.east());
        placeBlock(playerPos.west());
        
        if (doubleHeight.getValue()) {
            placeBlock(playerPos.north().above());
            placeBlock(playerPos.south().above());
            placeBlock(playerPos.east().above());
            placeBlock(playerPos.west().above());
        }
    }
    
    private void placeBlock(BlockPos pos) {
        if (mc.level == null || mc.player == null) return;
        if (!mc.level.getBlockState(pos).isCollisionShapeFullBlock(mc.level, pos)) return;
        
        BlockHitResult hit = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), 
            net.minecraft.core.Direction.UP, pos, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
    }
}
