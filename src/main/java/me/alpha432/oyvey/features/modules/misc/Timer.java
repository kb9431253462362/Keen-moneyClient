package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class Timer extends Module {
    public Setting<Double> speed = this.register(new Setting<>("Speed", 1.0, 0.1, 5.0));
    
    public Timer() {
        super("Timer", "Speed up or slow down game time", Category.MISC);
    }
    
    @Override
    public void onTick() {
        // This would be handled via mixin in the timer tick
        // Adjusts tick speed multiplier
    }
    
    public double getTimerSpeed() {
        return speed.getValue();
    }
}
