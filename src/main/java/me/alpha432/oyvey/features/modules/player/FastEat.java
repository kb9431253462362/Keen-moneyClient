package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class FastEat extends Module {
    private final Setting<Integer> speed = num("Speed", 16, 1, 32);

    public FastEat() {
        super("FastEat", "Eats food faster.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck() || !mc.player.isUsingItem()) return;
        for (int i = 0; i < speed.getValue() - 1; i++) {
            mc.gameMode.releaseUsingItem(mc.player);
            mc.gameMode.useItem(mc.player, mc.player.getUsedItemHand());
        }
    }
}
