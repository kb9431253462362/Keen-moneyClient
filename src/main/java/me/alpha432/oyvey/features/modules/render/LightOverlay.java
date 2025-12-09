package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class LightOverlay extends Module {
    public Setting<Double> brightness = this.register(new Setting<>("Brightness", 0.5, 0.0, 1.0));
    
    public LightOverlay() {
        super("LightOverlay", "Shows light level overlay", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null || mc.player == null) return;
        
        // Get light level at player position
        int lightLevel = mc.level.getLightEmission(mc.player.blockPosition());
        
        // Would render overlay in render event
        // Overlay would show light levels as colored squares
    }
}
