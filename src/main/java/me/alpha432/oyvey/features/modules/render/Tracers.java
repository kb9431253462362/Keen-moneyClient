package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.awt.*;

public class Tracers extends Module {
    public Setting<Color> color = color("Color", 255, 255, 255, 120);
    public Setting<Boolean> players = bool("Players", true);
    public Setting<Boolean> mobs = bool("Mobs", false);

    public Tracers() {
        super("Tracers", "Draws lines to entities.", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.level == null) return;
        for (Entity e : mc.level.entitiesForRendering()) {
            if (!(e instanceof LivingEntity) || e == mc.player) continue;
            boolean isPlayer = e instanceof net.minecraft.world.entity.player.Player;
            if ((players.getValue() && isPlayer) || (mobs.getValue() && !isPlayer)) {
                drawTracerLine(event, e);
            }
        }
    }

    private void drawTracerLine(Render3DEvent event, Entity target) {
        // Tracer rendering would use event.getMatrix() here
    }
}
