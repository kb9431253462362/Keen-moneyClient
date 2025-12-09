package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.entity.projectile.FishingHook;

public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish", "Automatically catches fish when fishing.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck() || mc.player.fishing == null) return;
        FishingHook hook = mc.player.fishing;
        if (hook.onGround()) {
            mc.gameMode.releaseUsingItem(mc.player);
        }
    }
}
