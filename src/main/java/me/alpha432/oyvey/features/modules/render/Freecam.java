package me.alpha432.oyvey.features.modules.render;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class Freecam extends Module {
    public Setting<Double> speed = this.register(new Setting<>("Speed", 1.0, 0.0, 2.0));
    private double camX, camY, camZ;
    private boolean initialized = false;
    
    public Freecam() {
        super("Freecam", "Detach camera from player body", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        if (mc.player != null) {
            camX = mc.player.getX();
            camY = mc.player.getY();
            camZ = mc.player.getZ();
            initialized = true;
        }
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        if (!initialized) {
            camX = mc.player.getX();
            camY = mc.player.getY();
            camZ = mc.player.getZ();
            initialized = true;
        }
        
        // Move camera with WASD
        var look = mc.player.getLookAngle();
        if (mc.options.keyUp.isDown()) {
            camX += look.x * speed.getValue() * 0.1;
            camY += look.y * speed.getValue() * 0.1;
            camZ += look.z * speed.getValue() * 0.1;
        }
        if (mc.options.keyDown.isDown()) {
            camX -= look.x * speed.getValue() * 0.1;
            camY -= look.y * speed.getValue() * 0.1;
            camZ -= look.z * speed.getValue() * 0.1;
        }
        
        // This is a simplified freecam - actual implementation requires camera modification
    }
}
