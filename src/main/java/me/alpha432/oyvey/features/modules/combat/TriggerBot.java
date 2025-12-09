package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.InteractionHand;

public class TriggerBot extends Module {
    private final Setting<Integer> delay = num("DelayMS", 50, 0, 500);
    private final Setting<Boolean> playersOnly = bool("PlayersOnly", true);

    private long last = 0L;

    public TriggerBot() {
        super("TriggerBot", "Automatically attacks entities under your crosshair after a short delay", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        EntityHitResult ehr = mc.hitResult instanceof EntityHitResult er ? er : null;
        if (ehr == null) return;
        if (!(ehr.getEntity() instanceof LivingEntity)) return;
        if (playersOnly.getValue() && !(ehr.getEntity() instanceof net.minecraft.world.entity.player.Player)) return;

        long now = System.currentTimeMillis();
        if (now - last >= delay.getValue()) {
            mc.gameMode.attack(mc.player, ehr.getEntity());
            mc.player.swing(InteractionHand.MAIN_HAND);
            last = now;
        }
    }
}
