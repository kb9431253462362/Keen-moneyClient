package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class Chams extends Module {
    public Setting<Double> alpha = this.register(new Setting<>("Alpha", 0.5, 0.0, 1.0));
    public Setting<Boolean> rainbow = this.register(new Setting<>("Rainbow", false));
    
    public Chams() {
        super("Chams", "See through walls with colored players", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null) return;
        
        // Adjust player rendering transparency for chams effect
        // Mainly handled in render events with custom shader
    }
}
