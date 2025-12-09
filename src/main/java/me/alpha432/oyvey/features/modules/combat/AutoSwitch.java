package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoSwitch extends Module {
    private final Setting<Boolean> onlyWhenTarget = bool("OnlyWhenTarget", true);
    private final Setting<Double> range = num("Range", 4.5, 1.0, 7.0);

    public AutoSwitch() {
        super("AutoSwitch", "Auto-switches to a sword in the hotbar when attacking", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        boolean need = true;
        if (onlyWhenTarget.getValue()) {
            need = mc.crosshairPickEntity != null || mc.hitResult instanceof net.minecraft.world.phys.EntityHitResult;
        }

        if (!need) return;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (isSword(stack)) {
                switchToSlot(i);
                return;
            }
        }
    }

    private boolean isSword(ItemStack stack) {
        return stack.is(Items.DIAMOND_SWORD) || stack.is(Items.IRON_SWORD) || 
               stack.is(Items.GOLDEN_SWORD) || stack.is(Items.NETHERITE_SWORD) || 
               stack.is(Items.STONE_SWORD) || stack.is(Items.WOODEN_SWORD);
    }

    private void switchToSlot(int slot) {
        mc.player.getInventory().setItem(45, mc.player.getInventory().getItem(slot).copy());
        mc.player.getInventory().setItem(slot, ItemStack.EMPTY);
    }
}
