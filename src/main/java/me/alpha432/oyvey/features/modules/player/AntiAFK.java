package me.alpha432.oyvey.features.modules.player;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class AntiAFK extends Module {
    public Setting<Integer> rotationSpeed = this.register(new Setting<>("RotationSpeed", 5, 1, 20));
    private int rotationTimer = 0;
    
    public AntiAFK() {
        super("AntiAFK", "Prevents AFK timeout", Category.PLAYER);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        rotationTimer++;
        if (rotationTimer < rotationSpeed.getValue()) return;
        rotationTimer = 0;
        
        // Rotate player slightly to prevent timeout
        mc.player.setYRot(mc.player.getYRot() + 1);
    }
}
