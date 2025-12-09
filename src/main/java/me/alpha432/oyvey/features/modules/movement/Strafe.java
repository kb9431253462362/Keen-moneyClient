package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
public class Strafe extends Module {
    public Setting<Double> speed = this.register(new Setting<>("Speed", 1.2, 0.5, 2.0));
    public Strafe() {
        super("Strafe", "Automatic strafing in air", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.onGround()) return;
        
        var vel = mc.player.getDeltaMovement();
        mc.player.setDeltaMovement(vel.x * speed.getValue(), vel.y, vel.z * speed.getValue());
    }
}
