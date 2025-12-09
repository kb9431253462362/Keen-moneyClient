package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class Fullbright extends Module {
    public Setting<Double> brightness = this.register(new Setting<>("Brightness", 1.0, 0.0, 2.0));
    
    public Fullbright() {
        super("Fullbright", "Increase brightness in dark areas", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // Set gamma to brighten the world
        mc.options.gamma().set(brightness.getValue());
    }
}
