package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class Reach extends Module {

    private final Setting<Double> range = num("Range", 5.0, 3.0, 7.0);

    public Reach() {
        super("Reach", "Increases block and entity interaction distance.", Category.PLAYER);
    }

    // This module typically requires a Mixin to modify the vanilla reach distance 
    // variable (e.g., ClientPlayerInteractionManager.getReachDistance).
    // The onTick is for management only.
    
    @Override
    public void onTick() {
        if (nullCheck()) return;
    }
}
