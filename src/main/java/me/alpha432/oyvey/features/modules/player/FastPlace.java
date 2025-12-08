package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", "Removes the cooldown when placing blocks.", Category.PLAYER);
    }

    @Override
   @Override
    public void onTick() {
        if (nullCheck()) return;

        // FIX: Directly set the item use delay field
        mc.player.itemUseCooldown = 0;
    }
}
