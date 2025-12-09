package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;

public class AutoTotem extends Module {
    private final Setting<Float> healthThreshold = num("HealthThreshold", 8.0f, 1.0f, 36.0f);
    private final Setting<Boolean> elytra = bool("InElytra", true);

    public AutoTotem() {
        super("AutoTotem", "Automatically equips a totem in your offhand.", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        // Check if we should hold totem
        boolean shouldHold = mc.player.getHealth() <= healthThreshold.getValue();
        boolean inElytra = elytra.getValue() && mc.player.isFallFlying();

        if (!shouldHold && !inElytra) {
            // Don't need totem, but keep checking if we have one
            if (!mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                return;
            }
        }

        // Always ensure we have totem in offhand if health is low or in elytra
        if (shouldHold || inElytra) {
            // Check if offhand already has totem
            if (mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                return;
            }

            // Find totem in hotbar (slots 0-8)
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getItem(i).is(Items.TOTEM_OF_UNDYING)) {
                    // Swap with offhand
                    mc.player.setItemInHand(InteractionHand.OFF_HAND, mc.player.getInventory().getItem(i).copy());
                    mc.player.getInventory().setItem(i, net.minecraft.world.item.ItemStack.EMPTY);
                    return;
                }
            }

            // If no totem in hotbar, check rest of inventory
            for (int i = 9; i < mc.player.getInventory().getContainerSize(); i++) {
                if (mc.player.getInventory().getItem(i).is(Items.TOTEM_OF_UNDYING)) {
                    mc.player.setItemInHand(InteractionHand.OFF_HAND, mc.player.getInventory().getItem(i).copy());
                    mc.player.getInventory().setItem(i, net.minecraft.world.item.ItemStack.EMPTY);
                    return;
                }
            }
        }
    }
}
