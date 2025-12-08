package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;

public class AutoGapple extends Module {
    
    private final Setting<Float> health = num("Health", 15.0f, 1.0f, 36.0f);
    
    public AutoGapple() {
        super("AutoGapple", "Automatically consumes God Apples.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck() || mc.player.isCreative()) return;

        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= health.getValue()) {
            
            int gappleSlot = -1;
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getItem(i).getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                    gappleSlot = i;
                    break;
                }
            }
            
            if (gappleSlot != -1) {
                // FIX: Use the local variable 'selected' to store and restore the current slot.
                int oldSlot = mc.player.getInventory().selected; 
                
                // FIX: Use the setter method
                mc.player.getInventory().setSelected(gappleSlot);
                
                mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                
                // FIX: Use the setter method
                mc.player.getInventory().setSelected(oldSlot);
            }
        }
    }
}
