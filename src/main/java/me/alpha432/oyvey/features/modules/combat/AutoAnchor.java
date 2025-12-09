package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class AutoAnchor extends Module {
    private final Setting<Double> radius = num("Radius", 12.0, 1.0, 16.0);
    private final Setting<Integer> breakDelay = num("BreakDelay", 5, 0, 20);

    private int breakTimer = 0;
    private LivingEntity bestTarget = null;

    public AutoAnchor() {
        super("AutoAnchor", "Automatically charges respawn anchors on enemies.", Category.COMBAT);
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
            breakAnchors();
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

    private void breakAnchors() {
        // Just a placeholder for anchor breaking
        breakTimer = breakDelay.getValue();
    }
}
