package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.item.Items;

public class AutoPot extends Module {
    public Setting<Double> healthThreshold = this.register(new Setting<>("HealthThreshold", 8.0, 0.0, 20.0));
    private int potionCooldown = 0;
    
    public AutoPot() {
        super("AutoPot", "Automatically drink healing potions", Category.MISC);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        potionCooldown--;
        if (potionCooldown > 0) return;
        
        // Check if player health is below threshold
        if (mc.player.getHealth() < healthThreshold.getValue()) {
            // Find healing potion in inventory
            for (int i = 0; i < 9; i++) {
                var item = mc.player.getInventory().getItem(i);
                if (item.getItem() == Items.POTION || item.getItem() == Items.SPLASH_POTION) {
                    // Switch to potion slot via packet
                    potionCooldown = 10;
                    break;
                }
            }
        }
    }
}
