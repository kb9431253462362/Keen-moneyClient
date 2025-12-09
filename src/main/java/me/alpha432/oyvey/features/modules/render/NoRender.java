package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class NoRender extends Module {
    public Setting<Boolean> noWeather = this.register(new Setting<>("NoWeather", true));
    public Setting<Boolean> noParticles = this.register(new Setting<>("NoParticles", false));
    
    public NoRender() {
        super("NoRender", "Disable various rendering elements", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null) return;
        
        // Disable rain/snow rendering (client-side)
        if (noWeather.getValue()) {
            // Handled by mixin or event
        }
        
        // Clear particles if enabled (handled via event listeners)
        if (noParticles.getValue()) {
            // Particles cleared via render event
        }
    }
}
