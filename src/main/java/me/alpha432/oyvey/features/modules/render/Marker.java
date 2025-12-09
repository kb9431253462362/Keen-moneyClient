package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import java.util.ArrayList;
import java.util.List;

public class Marker extends Module {
    public Setting<Boolean> render = this.register(new Setting<>("Render", true));
    private List<MarkerData> markers = new ArrayList<>();
    
    public Marker() {
        super("Marker", "Mark locations with waypoints", Category.RENDER);
    }
    
    @Override
    public void onTick() {
        // Maintain marker list
        // Can be updated by key bindings
    }
    
    public void addMarker(double x, double y, double z, String name) {
        markers.add(new MarkerData(x, y, z, name));
    }
    
    public List<MarkerData> getMarkers() {
        return render.getValue() ? markers : new ArrayList<>();
    }
    
    public static class MarkerData {
        public double x, y, z;
        public String name;
        public MarkerData(double x, double y, double z, String name) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.name = name;
        }
    }
}
