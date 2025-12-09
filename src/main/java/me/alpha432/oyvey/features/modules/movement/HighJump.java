package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
public class HighJump extends Module {
    public Setting<Double> height = this.register(new Setting<>("Height", 1.5, 0.5, 3.0));
    public HighJump() {
        super("HighJump", "Jump higher than normal", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.onGround()) return;
        
        // Boost jump height when player tries to jump
        if (mc.options.keyJump.isDown()) {
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, height.getValue(), mc.player.getDeltaMovement().z);
        }
    }
}
