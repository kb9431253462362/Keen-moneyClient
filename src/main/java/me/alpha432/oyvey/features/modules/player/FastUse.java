package me.alpha432.oyvey.features.modules.player;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class FastUse extends Module {
    public Setting<Double> speed = this.register(new Setting<>("Speed", 2.0, 1.0, 5.0));
    
    public FastUse() {
        super("FastUse", "Use items faster", Category.PLAYER);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // Speed up item usage (bows, potions, food)
        mc.player.getUseItemRemainingTicks();
        if (mc.player.isUsingItem()) {
            // This would reduce use ticks in reality through mixin
        }
    }
}
