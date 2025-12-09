package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
public class ElytraBoost extends Module {
    public Setting<Double> boostPower = this.register(new Setting<>("BoostPower", 1.0, 0.0, 2.0));
    public ElytraBoost() {
        super("ElytraBoost", "Boost elytra flight", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isFallFlying()) return;
        
        var look = mc.player.getLookAngle();
        mc.player.setDeltaMovement(
            mc.player.getDeltaMovement().x + look.x * boostPower.getValue() * 0.1,
            mc.player.getDeltaMovement().y + look.y * boostPower.getValue() * 0.1,
            mc.player.getDeltaMovement().z + look.z * boostPower.getValue() * 0.1
        );
    }
}
