package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", "Removes the cooldown when placing blocks.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

          mc.rightClickDelay = 0;
      
    }
}
