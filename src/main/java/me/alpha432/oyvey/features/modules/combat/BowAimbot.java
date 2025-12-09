package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class BowAimbot extends Module {
    public Setting<Double> fov = this.register(new Setting<>("FOV", 45.0, 0.0, 90.0));
    public BowAimbot() {
        super("BowAimbot", "Automatically aims your bow at enemies", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (mc.player.getMainHandItem().getItem() != Items.BOW) return;
        
        // Find closest enemy in FOV
        mc.level.players().forEach(entity -> {
            if (entity.equals(mc.player)) return;
            if (entity.distanceTo(mc.player) > 50) return;
            
            Vec3 toEntity = entity.position().subtract(mc.player.position());
            Vec3 lookDir = mc.player.getLookAngle();
            
            double dotProduct = toEntity.normalize().dot(lookDir);
            if (dotProduct > Math.cos(Math.toRadians(fov.getValue()))) {
                // Aim at entity
                double yaw = Math.atan2(toEntity.z, toEntity.x) * 180 / Math.PI - 90;
                double pitch = -Math.atan2(toEntity.y, new Vec3(toEntity.x, 0, toEntity.z).length()) * 180 / Math.PI;
                mc.player.setYRot((float) yaw);
                mc.player.setXRot((float) pitch);
            }
        });
    }
}
