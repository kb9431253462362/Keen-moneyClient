package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BedAura extends Module {
    public Setting<Double> range = this.register(new Setting<>("Range", 5.0, 0.0, 16.0));
    public Setting<Double> speed = this.register(new Setting<>("Speed", 1.0, 0.0, 5.0));
    public Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<>("Blue", 0, 0, 255));

    public BedAura() {
        super("BedAura", "Automatically places and breaks beds on enemies", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        Player target = this.findNearestPlayer(this.range.getValue());
        if (target == null) return;
        
        Vec3 targetPos = target.position();
    }

    private Player findNearestPlayer(double range) {
        Player closest = null;
        double closestDist = range;
        
        for (Player player : mc.level.players()) {
            if (player == mc.player) continue;
            double dist = mc.player.distanceToSqr(player);
            if (dist < closestDist * closestDist) {
                closestDist = Math.sqrt(dist);
                closest = player;
            }
        }
        return closest;
    }
}
