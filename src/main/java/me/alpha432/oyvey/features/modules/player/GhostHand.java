package me.alpha432.oyvey.features.modules.player;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class GhostHand extends Module {
    public Setting<Boolean> fixArmor = this.register(new Setting<>("FixArmor", true));
    public Setting<Boolean> hideHand = this.register(new Setting<>("HideHand", false));
    
    public GhostHand() {
        super("GhostHand", "Hand visibility customization", Category.PLAYER);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        // This would be handled in rendering events
        // Can hide first-person hand or fix armor position
        if (hideHand.getValue()) {
            // Hide hand rendering
        }
        if (fixArmor.getValue()) {
            // Fix armor rendering position
        }
    }
}
