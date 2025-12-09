package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

public class AutoEXP extends Module {
    public Setting<Integer> minThreshold = this.register(new Setting<>("MinThreshold", 30, 1, 100));
    public Setting<Integer> maxThreshold = this.register(new Setting<>("MaxThreshold", 80, 1, 100));
    private int repairingSlot = -1;
    
    public AutoEXP() {
        super("AutoEXP", "Automatically repairs gear with experience bottles", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        // Find item needing repair
        if (repairingSlot == -1) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.isArmor()) {
                    var stack = mc.player.getItemBySlot(slot);
                    if (needsRepair(stack, minThreshold.getValue())) {
                        repairingSlot = 36 + slot.getIndex(); // Armor slots 36-39
                        break;
                    }
                }
            }
        }
        
        // Repair the item
        if (repairingSlot != -1) {
            var armorStack = mc.player.getInventory().getItem(repairingSlot);
            if (!needsRepair(armorStack, maxThreshold.getValue())) {
                repairingSlot = -1;
                return;
            }
            
            // Find experience bottle
            for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
                var slot = mc.player.getInventory().getItem(i);
                if (slot.getItem() == Items.EXPERIENCE_BOTTLE) {
                    // Throw the bottle at ground
                    mc.player.getInventory().setItem(i, slot.copy());
                    mc.gameMode.useItem(mc.player, net.minecraft.world.InteractionHand.MAIN_HAND);
                    return;
                }
            }
        }
    }
    
    private boolean needsRepair(net.minecraft.world.item.ItemStack stack, double threshold) {
        if (stack.isEmpty() || !stack.isDamageableItem()) return false;
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        return (maxDamage - damage) / (double) maxDamage * 100 <= threshold;
    }
}
