package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class AutoRespawn extends Module {
    public Setting<Boolean> instant = this.register(new Setting<>("Instant", false));
    
    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawn when dead", Category.MISC);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // Check if player is dead
        if (mc.player.getHealth() <= 0 || mc.screen instanceof net.minecraft.client.gui.screens.DeathScreen) {
            // Send respawn packet
            mc.player.respawn();
        }
    }
}
