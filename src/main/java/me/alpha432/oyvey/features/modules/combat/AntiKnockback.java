package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class AntiKnockback extends Module {
    private final Setting<Float> reduction = num("Reduction", 0.5f, 0.0f, 1.0f);

    public AntiKnockback() {
        super("AntiKnockback", "Reduces knockback from attacks.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mc.player.hurtTime > 0) {
            double motX = mc.player.getDeltaMovement().x;
            double motY = mc.player.getDeltaMovement().y;
            double motZ = mc.player.getDeltaMovement().z;
            mc.player.setDeltaMovement(motX * reduction.getValue(), motY, motZ * reduction.getValue());
        }
    }
}
