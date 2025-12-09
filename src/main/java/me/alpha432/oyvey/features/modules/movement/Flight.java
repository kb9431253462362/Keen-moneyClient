package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.entity.player.Abilities;

public class Flight extends Module {
    // You can adjust this value to change the flight speed. Default is 0.05f.
    private final float DEFAULT_FLIGHT_SPEED = 0.05f;
    public Flight() {

        // Renamed to "Flight" as requested.

        super("Flight", "Uses vanilla flight mechanics, reinforced every tick.", Category.MOVEMENT);

    }

    @Override
    public void onEnable() {

        if (mc.player == null) return;
        Abilities abilities = mc.player.getAbilities();
        if (abilities.instabuild || mc.player.isSpectator()) return;
        abilities.mayfly = true;          
        abilities.flying = true;       
        abilities.setFlyingSpeed(DEFAULT_FLIGHT_SPEED);
        mc.player.onUpdateAbilities();    
    }







    @Override
    public void onDisable() {

        if (mc.player == null) return;
        Abilities abilities = mc.player.getAbilities();
        // Revert abilities only if the player wasn't already flying in Creative/Spectator
        if (!abilities.instabuild && !mc.player.isSpectator()) {
            abilities.mayfly = false;
            abilities.flying = false;
            abilities.setFlyingSpeed(DEFAULT_FLIGHT_SPEED);

        }

        mc.player.onUpdateAbilities();

    }

    @Override

    public void onTick() {
        if (mc.player != null) {

            Abilities abilities = mc.player.getAbilities();

            if (!abilities.instabuild && !mc.player.isSpectator()) {

                if (!abilities.mayfly) {
                    abilities.mayfly = true;
                    abilities.setFlyingSpeed(DEFAULT_FLIGHT_SPEED);

                }

            }

        }

    }

}