package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.InteractionHand;

public class SmartCrits extends Module {
    private final Setting<Integer> delay = num("DelayMS", 100, 0, 500);
    private final Setting<Boolean> autoHit = bool("AutoHit", true);
    private long lastCrit = 0L;

    public SmartCrits() {
        super("SmartCrits", "Attempts to land critical hits more reliably.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        EntityHitResult ehr = mc.hitResult instanceof EntityHitResult e ? e : null;
        if (ehr == null || !(ehr.getEntity() instanceof LivingEntity)) return;

        long now = System.currentTimeMillis();
        // Only jump if on ground and delay has passed
        if (mc.player.onGround() && now - lastCrit >= delay.getValue()) {
            mc.player.jumpFromGround();
            
            if (autoHit.getValue()) {
                mc.gameMode.attack(mc.player, (LivingEntity) ehr.getEntity());
                mc.player.swing(InteractionHand.MAIN_HAND);
            }
            lastCrit = now;
        }
    }
}
