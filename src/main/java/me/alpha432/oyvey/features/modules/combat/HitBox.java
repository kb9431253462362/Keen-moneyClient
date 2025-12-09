package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import java.util.Comparator;

public class HitBox extends Module {
    private final Setting<Double> expand = num("Expand", 0.3, 0.0, 2.0);
    private final Setting<Double> range = num("Range", 4.5, 1.0, 7.0);
    private final Setting<Boolean> players = bool("Players", true);
    private final Setting<Boolean> mobs = bool("Mobs", true);

    public HitBox() {
        super("HitBox", "Expands selection area for targets", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        
        LivingEntity target = findTarget();
        if (target != null && mc.crosshairPickEntity == null) {
            // Update the picked entity for rendering
            mc.crosshairPickEntity = target;
        }
    }

    public LivingEntity findTarget() {
        AABB search = mc.player.getBoundingBox().inflate(range.getValue());
        return (LivingEntity) mc.level.getEntities(mc.player, search, e ->
                e instanceof LivingEntity && !e.isSpectator() && !e.equals(mc.player) &&
                        ((players.getValue() && e instanceof Player && !(e instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon)) || 
                         (mobs.getValue() && (e instanceof Monster || (!(e instanceof Player)))))
        ).stream().filter(e -> {
            AABB box = e.getBoundingBox().inflate(expand.getValue());
            return box.intersects(mc.player.getBoundingBox().inflate(range.getValue()));
        }).min(Comparator.comparingDouble(mc.player::distanceToSqr)).orElse(null);
    }
}
