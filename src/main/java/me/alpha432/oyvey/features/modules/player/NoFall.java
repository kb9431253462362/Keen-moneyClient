package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

import static me.alpha432.oyvey.util.traits.Util.mc;

public class NoFall extends Module {
    public Setting<Mode> mode = mode("Mode", Mode.Packet);
    public Setting<Integer> threshold = num("Threshold", 3, 1, 20);

    public NoFall() {
        super("NoFall", "Removes fall damage", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;

        double fall = OyVey.positionManager.getFallDistance();
        if (!mc.player.onGround() && fall > threshold.getValue()) {
            // Safer packet strategies to reset fall distance server-side
            switch (mode.getValue()) {
                case Packet -> sendPacketMode();
                case Spoof -> sendSpoofMode();
            }
        }
    }

    private void sendPacketMode() {
        // Common bypass: send a tiny upward packet then a ground packet to reset fall
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        // send a small offset (looks like normal movement)
        OyVey.positionManager.setPositionPacket(x, y + 0.0625, z, false, false, false);
        // then send an onGround packet so server treats us as landed
        OyVey.positionManager.setPositionPacket(x, y, z, true, false, false);
    }

    private void sendSpoofMode() {
        // Spoof mode: send only an onGround=true packet (less noisy)
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        OyVey.positionManager.setPositionPacket(x, y, z, true, false, false);
    }

    public enum Mode {
        Packet,
        Spoof
    }
}
