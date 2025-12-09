package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically toggles sprinting.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        double forward = mc.player.xxa;
        double strafe = mc.player.zza;
        if ((forward != 0 || strafe != 0) && !mc.player.isInWater() && !mc.player.isInLava()) {
            mc.player.setSprinting(true);
        }
    }
}
