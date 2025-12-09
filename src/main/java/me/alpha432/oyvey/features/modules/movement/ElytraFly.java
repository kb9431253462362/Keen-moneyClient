package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
public class ElytraFly extends Module {
    public Setting<Double> speed = this.register(new Setting<>("Speed", 1.0, 0.0, 3.0));
    public ElytraFly() {
        super("ElytraFly", "Fly with elytra", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isFallFlying()) return;
        
        var vel = mc.player.getDeltaMovement();
        mc.player.setDeltaMovement(vel.x * speed.getValue(), vel.y, vel.z * speed.getValue());
    }
}
