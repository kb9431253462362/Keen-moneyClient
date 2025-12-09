package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.projectile.Arrow;

public class ArrowDodge extends Module {
    public Setting<Double> prediction = this.register(new Setting<>("Prediction", 0.5, 0.0, 2.0));
    public ArrowDodge() {
        super("ArrowDodge", "Dodges incoming arrows", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        mc.level.getEntities(null, mc.player.getBoundingBox().inflate(10)).forEach(entity -> {
            if (!(entity instanceof Arrow)) return;
            Arrow arrow = (Arrow) entity;
            
            // Move away from arrow
            var vel = arrow.getDeltaMovement();
            var direction = vel.normalize();
            
            // Jump and strafe
            if (Math.abs(direction.x) > 0.3 || Math.abs(direction.z) > 0.3) {
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.5, mc.player.getDeltaMovement().z);
                var moveDir = new net.minecraft.world.phys.Vec3(-direction.x * prediction.getValue(), 0, -direction.z * prediction.getValue());
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(moveDir));
            }
        });
    }
}
