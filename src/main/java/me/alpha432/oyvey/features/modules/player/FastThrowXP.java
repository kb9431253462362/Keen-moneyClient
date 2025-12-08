package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.Items;

public class FastThrowXP extends Module {
    public FastThrowXP() {
        super("FastThrowXP", "Makes you throw exp faster", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (mc.player.isHolding(Items.EXPERIENCE_BOTTLE)) {
            mc.rightClickDelay = 0;
        }
    }
}

