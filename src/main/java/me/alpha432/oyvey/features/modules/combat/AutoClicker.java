package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.InteractionHand;

public class AutoClicker extends Module {
    private final Setting<Integer> cps = num("CPS", 8, 1, 20);
    private final Setting<Boolean> leftClick = bool("LeftClick", true);
    private final Setting<Boolean> rightClick = bool("RightClick", false);
    private final Setting<Mode> mode = mode("Mode", Mode.Auto);

    private long last = 0L;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks when aiming at an entity", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        boolean shouldClick = mode.getValue() == Mode.Auto || mc.options.keyAttack.isDown();
        if (!shouldClick) return;

        EntityHitResult ehr = mc.hitResult instanceof EntityHitResult er ? er : null;
        if (ehr == null) return;
        if (!(ehr.getEntity() instanceof LivingEntity)) return;

        long delay = (long) (1000.0 / cps.getValue());
        long now = System.currentTimeMillis();
        if (now - last >= delay) {
            LivingEntity target = (LivingEntity) ehr.getEntity();
            
            if (leftClick.getValue()) {
                mc.gameMode.attack(mc.player, target);
                mc.player.swing(InteractionHand.MAIN_HAND);
            }
            
            if (rightClick.getValue()) {
                mc.player.swing(InteractionHand.MAIN_HAND);
            }
            
            last = now;
        }
    }

    public enum Mode {
        Auto,
        OnKey
    }
}
