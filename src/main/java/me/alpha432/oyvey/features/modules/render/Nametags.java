package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Nametags extends Module {
    public Setting<Boolean> showName = bool("ShowName", true);
    public Setting<Boolean> showArmor = bool("ShowArmor", true);
    public Setting<Boolean> showMainHand = bool("ShowMainHand", true);
    public Setting<Float> scale = num("Scale", 1.0f, 0.5f, 2.0f);
    public Setting<Float> maxDistance = num("MaxDistance", 64.0f, 4.0f, 256.0f);

    public Nametags() {
        super("Nametags", "Shows player names, armor, and items above their head.", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.level == null) return;
        for (Entity e : mc.level.entitiesForRendering()) {
            if (!(e instanceof Player) || e == mc.player) continue;
            if (mc.player.distanceTo(e) > maxDistance.getValue()) continue;

            Player player = (Player) e;
            renderPlayerInfo(event, player);
        }
    }

    private void renderPlayerInfo(Render3DEvent event, Player player) {
        Vec3 headPos = player.getEyePosition().add(0, 0.5, 0);
        
        // Line 1: Player name (top)
        String nameText = "";
        if (showName.getValue()) {
            nameText = player.getDisplayName().getString();
        }
        
        // Line 2: Armor items (horizontal row: Boots, Leggings, Chestplate, Helmet) + Main hand
        StringBuilder itemLine = new StringBuilder();
        if (showArmor.getValue()) {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            
            if (!boots.isEmpty()) itemLine.append(boots.getHoverName().getString()).append(" ");
            if (!leggings.isEmpty()) itemLine.append(leggings.getHoverName().getString()).append(" ");
            if (!chestplate.isEmpty()) itemLine.append(chestplate.getHoverName().getString()).append(" ");
            if (!helmet.isEmpty()) itemLine.append(helmet.getHoverName().getString()).append(" ");
        }
        
        if (showMainHand.getValue()) {
            ItemStack mainHand = player.getMainHandItem();
            if (!mainHand.isEmpty()) {
                itemLine.append(mainHand.getHoverName().getString());
            }
        }
        
        // Text rendering would use event.getMatrix() and screen projection here
        // Format:
        //          Name
        // boots pants chestplate helmet MainHand
    }
}
