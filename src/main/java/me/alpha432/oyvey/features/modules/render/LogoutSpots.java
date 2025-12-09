package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import java.util.HashMap;
import java.util.Map;

public class LogoutSpots extends Module {
    public Setting<Double> range = this.register(new Setting<>("Range", 50.0, 10.0, 200.0));
    private Map<String, LogoutData> logoutSpots = new HashMap<>();
    
    public LogoutSpots() {
        super("LogoutSpots", "Mark player logout positions", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        if (mc.level == null || mc.player == null) return;
        
        // Store player positions for logout detection
        mc.level.players().forEach(entity -> {
            if (!entity.equals(mc.player) && entity.distanceTo(mc.player) < range.getValue()) {
                logoutSpots.put(entity.getName().getString(), 
                    new LogoutData(entity.getX(), entity.getY(), entity.getZ()));
            }
        });
    }
    
    public static class LogoutData {
        public double x, y, z;
        public long timestamp = System.currentTimeMillis();
        public LogoutData(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
