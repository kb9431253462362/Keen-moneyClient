package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
public class TridentBoost extends Module {
    public Setting<Double> boostMultiplier = this.register(new Setting<>("BoostMult", 1.5, 0.5, 3.0));
    public TridentBoost() {
        super("TridentBoost", "Boost with trident", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isInWater()) return;
        
        var vel = mc.player.getDeltaMovement();
        mc.player.setDeltaMovement(vel.x * boostMultiplier.getValue(), vel.y, vel.z * boostMultiplier.getValue());
    }
}
