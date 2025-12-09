package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import java.util.HashMap;
import java.util.Map;

public class Trail extends Module {
    public Setting<Integer> duration = this.register(new Setting<>("Duration", 20, 5, 60));
    private Map<Integer, Long> playerTrails = new HashMap<>();
    
    public Trail() {
        super("Trail", "Draw trails behind players", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null) return;
        
        long currentTime = System.currentTimeMillis();
        
        // Track player positions
        mc.level.players().forEach(entity -> {
            if (!entity.equals(mc.player)) {
                playerTrails.put(entity.getId(), currentTime);
            }
        });
        
        // Remove old trail data
        playerTrails.values().removeIf(time -> currentTime - time > duration.getValue() * 1000);
    }
}
