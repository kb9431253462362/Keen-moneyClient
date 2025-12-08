package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import org.lwjgl.input.Keyboard;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint", "Automatically sprints when you walk forward.", Category.MOVEMENT);
        this.setBind(Keyboard.KEY_F);
    }

    public void onEnable() {
        if (mc.player != null) { 
            mc.player.setSprinting(true);
        }
        super.onEnable();
    }

    public void onDisable() {
        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
        super.onDisable();
    }
}
