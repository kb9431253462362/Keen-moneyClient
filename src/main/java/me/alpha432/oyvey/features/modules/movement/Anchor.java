package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
public class Anchor extends Module {
    public Setting<Double> strength = this.register(new Setting<>("Strength", 1.0, 0.0, 2.0));
    public Anchor() {
        super("Anchor", "Anchor yourself to stay in place", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        mc.player.setDeltaMovement(0, mc.player.getDeltaMovement().y * (1.0 - strength.getValue()), 0);
    }
}
