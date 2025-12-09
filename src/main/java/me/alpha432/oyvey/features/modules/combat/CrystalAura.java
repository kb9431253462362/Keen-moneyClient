package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

public class CrystalAura extends Module {
    private final Setting<Double> radius = num("Radius", 12.0, 1.0, 16.0);
    private final Setting<Integer> breakDelay = num("BreakDelay", 2, 0, 20);

    private int breakTimer = 0;
    private LivingEntity bestTarget = null;

    public CrystalAura() {
        super("CrystalAura", "Automatically breaks end crystals on enemies.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        breakTimer--;
        findBestTarget();

        if (bestTarget == null || bestTarget.isDeadOrDying()) {
            bestTarget = null;
            return;
        }

        if (breakTimer <= 0) {
            breakCrystals();
        }
    }

    private void findBestTarget() {
        bestTarget = null;
        double closestDist = radius.getValue() * radius.getValue();

        try {
            for (net.minecraft.world.entity.Entity entity : mc.level.getEntities(null, mc.player.getBoundingBox().inflate(radius.getValue()))) {
                if (!(entity instanceof LivingEntity living) || entity == mc.player) continue;
                if (living.isDeadOrDying()) continue;
                if (living instanceof Player && ((Player) living).isCreative()) continue;

                double dist = mc.player.distanceToSqr(entity);
                if (dist < closestDist) {
                    bestTarget = living;
                    closestDist = dist;
                }
            }
        } catch (Exception e) {
            //
        }
    }

    private void breakCrystals() {
        try {
            for (net.minecraft.world.entity.Entity entity : mc.level.getEntities(null, mc.player.getBoundingBox().inflate(12))) {
                if (entity.getClass().getSimpleName().equals("EndCrystalEntity")) {
                    double dist = mc.player.distanceToSqr(entity);
                    if (dist > 20.25) continue;

                    mc.gameMode.attack(mc.player, entity);
                    mc.player.swing(InteractionHand.MAIN_HAND);
                    breakTimer = breakDelay.getValue();
                    return;
                }
            }
        } catch (Exception e) {
            //
        }
    }
}
