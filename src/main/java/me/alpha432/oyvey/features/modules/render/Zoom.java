package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class Zoom extends Module {
    public Setting<Double> zoomLevel = this.register(new Setting<>("ZoomLevel", 5.0, 1.0, 15.0));
    
    public Zoom() {
        super("Zoom", "Zoom in with your camera", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // Modify FOV to zoom in
        int fov = (int) (70 / zoomLevel.getValue());
        mc.options.fov().set(fov);
    }
    
    @Override
    public void onDisable() {
        // Reset FOV to default
        if (mc.options != null) {
            mc.options.fov().set(70);
        }
    }
}
