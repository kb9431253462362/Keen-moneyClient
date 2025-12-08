package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import java.util.Comparator;

public class Killaura extends Module {
    
    private final Setting<Double> range = num("Range", 4.0, 1.0, 7.0);
    private final Setting<Boolean> players = bool("Players", true);
    private final Setting<Boolean> mobs = bool("Mobs", false);

    // >>> FIX: Changed the incorrect call to 'enumSetting' to the likely correct 'enumSet'
    private final Setting<Mode> mode = register(new Setting<>("Mode", Mode.Auto));
    // ALTERNATIVELY, if your Module class has a helper function:
    // private final Setting<Mode> mode = enumSet("Mode", Mode.Auto);

    public Killaura() {
        super("Killaura", "Automatically attacks entities around you.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        LocalPlayer player = mc.player;

        // Use the new accessor method for attack cooldown
        boolean isAttackReady = player.getAttackStrengthScale(0.0F) >= 1.0F;
        boolean shouldActivate = mode.getValue() == Mode.Auto || (mode.getValue() == Mode.OnHitKey && mc.options.keyAttack.isDown());

        if (!isAttackReady || !shouldActivate) return;

        Entity target = findTarget();
        
        if (target != null) {
            mc.gameMode.attack(player, target);
            player.swing(InteractionHand.MAIN_HAND); 
        }
    }

    private Entity findTarget() {
        AABB searchBox = mc.player.getBoundingBox().inflate(range.getValue());
        
        return mc.level.getEntities(mc.player, searchBox, e -> 
            e instanceof LivingEntity && !e.isSpectator() && e.isAlive() && !e.equals(mc.player) && 
            (players.getValue() && e instanceof net.minecraft.world.entity.player.Player || mobs.getValue() && e instanceof net.minecraft.world.entity.monster.Monster)
        ).stream().min(Comparator.comparingDouble(mc.player::distanceToSqr)).orElse(null);
    }

    public enum Mode {
        Auto,
        OnHitKey
    }
}
