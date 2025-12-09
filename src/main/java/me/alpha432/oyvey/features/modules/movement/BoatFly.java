package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
public class BoatFly extends Module {
    public Setting<Double> speed = this.register(new Setting<>("Speed", 1.5, 0.0, 3.0));
    public BoatFly() {
        super("BoatFly", "Fly using boats", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.getVehicle() == null) return;
        
        var vel = mc.player.getVehicle().getDeltaMovement();
        mc.player.getVehicle().setDeltaMovement(vel.x * speed.getValue(), vel.y, vel.z * speed.getValue());
    }
}
