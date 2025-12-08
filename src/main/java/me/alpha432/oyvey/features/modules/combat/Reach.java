package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class Reach extends Module {

    private final Setting<Double> extraBlockReach = num("Extra Block Reach", 1.0, 0.0, 7.0);
    private final Setting<Double> extraEntityReach = num("Extra Entity Reach", 1.0, 0.0, 7.0);
    

    public Reach() {
        super("Reach", "Increases block and entity interaction distance.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        
    }

    public double getExtraBlockReach() {
        return isEnabled() ? extraBlockReach.getValue() : 0.0;
    }

    public double getExtraEntityReach() {
        return isEnabled() ? extraEntityReach.getValue() : 0.0;
    }
}
