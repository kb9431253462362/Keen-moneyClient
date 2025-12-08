package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", "Removes the cooldown when placing blocks.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // Directly set the block break/use delay to 0
        mc.gameMode.setDestroyProgress(0.0f); 
    }
}
