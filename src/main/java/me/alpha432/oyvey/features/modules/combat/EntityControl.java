package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class EntityControl extends Module {
    public Setting<Double> speed = this.register(new Setting<>("Speed", 1.0, 0.0, 2.0));
    public EntityControl() {
        super("EntityControl", "Control rideable entities", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.player.getVehicle() == null) return;
        
        var vehicle = mc.player.getVehicle();
        
        // Boost vehicle movement
        var vel = vehicle.getDeltaMovement();
        vehicle.setDeltaMovement(vel.x * speed.getValue(), vel.y, vel.z * speed.getValue());
    }
}
