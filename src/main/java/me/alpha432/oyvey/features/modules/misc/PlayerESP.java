package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.entity.player.Player;
import java.awt.Color;

public class PlayerESP extends Module {

    public PlayerESP() {
        super("PlayerESP", "Draws boxes around players.", Category.RENDER);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;

        for (Player player : mc.level.players()) {
            if (player.equals(mc.player)) continue;

            Color color = new Color(255, 0, 0, 180); // Red
            // RenderUtil.drawEntityBox(player, color);
        }
    }
}
