package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import java.util.HashMap;
import java.util.Map;

public class PopChams extends Module {
    public Setting<Integer> duration = this.register(new Setting<>("Duration", 5, 1, 20));
    private Map<Integer, Long> poppedPlayers = new HashMap<>();
    
    public PopChams() {
        super("PopChams", "Highlight recently popped players", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null) return;
        
        long currentTime = System.currentTimeMillis();
        
        // Check for players who just took damage (popped)
        mc.level.players().forEach(entity -> {
            if (!entity.equals(mc.player) && entity.getHealth() < entity.getMaxHealth()) {
                poppedPlayers.put(entity.getId(), currentTime);
            }
        });
        
        // Remove old pop data
        poppedPlayers.values().removeIf(time -> currentTime - time > duration.getValue() * 1000);
    }
}
