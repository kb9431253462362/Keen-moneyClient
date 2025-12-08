package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.player.LocalPlayer;
import java.util.Comparator;

public class Killaura extends Module {
    
    private final Setting<Double> range = num("Range", 4.0, 1.0, 7.0);
    private final Setting<Boolean> players = bool("Players", true);
    private final Setting<Boolean> mobs = bool("Mobs", false);

    public Killaura() {
        super("Killaura", "Automatically attacks entities around you.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        LocalPlayer player = mc.player;
        if (player.getAttackStrengthScale(0.0F) < 1.0F) return; // Wait for attack cooldown

        Entity target = findTarget();
        
        if (target != null) {
            mc.gameMode.attack(player, target);
            player.swing(target.swingingArm);
        }
    }

    private Entity findTarget() {
        AABB searchBox = mc.player.getBoundingBox().inflate(range.getValue());
        
        return mc.level.getEntities(mc.player, searchBox, e -> 
            e instanceof LivingEntity && !e.isSpectator() && e.isAlive() && !e.equals(mc.player) && 
            (players.getValue() && e instanceof net.minecraft.world.entity.player.Player || mobs.getValue() && e instanceof net.minecraft.world.entity.monster.Monster)
        ).stream().min(Comparator.comparingDouble(mc.player::distanceToSqr)).orElse(null);
    }
}
