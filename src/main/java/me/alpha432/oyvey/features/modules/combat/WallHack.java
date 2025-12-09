package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class WallHack extends Module {
    public Setting<Boolean> renderWalls = this.register(new Setting<>("RenderWalls", true));
    public WallHack() {
        super("WallHack", "See through walls", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        if (renderWalls.getValue()) {
            // Decrease rendering of walls - usually handled by render event
            // This is handled in rendering, not tick
        }
    }
}
