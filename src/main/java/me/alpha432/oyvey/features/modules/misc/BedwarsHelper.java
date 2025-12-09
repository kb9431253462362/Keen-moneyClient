package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class BedwarsHelper extends Module {
    public Setting<Double> range = this.register(new Setting<>("Range", 10.0, 0.0, 20.0));
    public Setting<Boolean> highlightBeds = this.register(new Setting<>("HighlightBeds", true));
    
    public BedwarsHelper() {
        super("BedwarsHelper", "Bedwars utility features", Category.MISC);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null || mc.player == null) return;
        
        // Find beds nearby
        if (highlightBeds.getValue()) {
            // Scan for beds within range
            // Would highlight them in render events
        }
    }
}
