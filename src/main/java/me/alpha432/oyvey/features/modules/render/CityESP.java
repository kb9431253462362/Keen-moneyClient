package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import java.util.HashSet;
import java.util.Set;

public class CityESP extends Module {
    public Setting<Double> range = this.register(new Setting<>("Range", 50.0, 10.0, 200.0));
    private Set<String> knownCities = new HashSet<>();
    
    public CityESP() {
        super("CityESP", "Highlights player bases and cities", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null || mc.player == null) return;
        
        // Track player-built structures (simplified)
        mc.level.players().forEach(entity -> {
            if (!entity.equals(mc.player) && entity.distanceTo(mc.player) < range.getValue()) {
                knownCities.add(entity.getName().getString());
            }
        });
    }
}
