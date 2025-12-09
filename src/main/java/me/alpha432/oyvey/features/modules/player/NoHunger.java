package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class NoHunger extends Module {
    public NoHunger() {
        super("NoHunger", "Prevents hunger loss.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        mc.player.getFoodData().setFoodLevel(20);
    }
}
