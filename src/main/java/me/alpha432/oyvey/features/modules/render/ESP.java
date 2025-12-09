package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon; 
import net.minecraft.world.entity.boss.wither.WitherBoss; 
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import  net.minecraft.world.entity.animal.wolf.*;
import net.minecraft.world.entity.animal.sheep.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ESP extends Module { // NOTE: Rename file to ESP.java if this is the public class name

    public Setting<Boolean> players = bool("Players", true);
    public Setting<Boolean> animals = bool("Animals", false);
    public Setting<Boolean> hostile = bool("Hostile", false);
    public Setting<Mode> highlightMode = mode("Mode", Mode.Box);
    public Setting<Color> boxColor = color("Box Color", 255, 0, 0, 150);
    public Setting<Color> outlineColor = color("Outline Color", 255, 0, 0, 255);
    public Setting<Float> lineWidth = num("LineWidth", 1.5f, 0.1f, 5.0f);

    public final Set<Class<? extends Entity>> mobTargets = new HashSet<>();

    public ESP() {
        super("EntityESP", "Highlights players and mobs in the world", Category.RENDER);
        
        // --- Populate the Mob List ---
        mobTargets.add(Zombie.class);
        mobTargets.add(Spider.class);
        mobTargets.add(Creeper.class);
        mobTargets.add(Skeleton.class);
    
        mobTargets.add(Ghast.class);
        mobTargets.add(Blaze.class);
        mobTargets.add(Witch.class);
        mobTargets.add(Vex.class);
        mobTargets.add(Shulker.class);
        mobTargets.add(EnderDragon.class); 
        mobTargets.add(WitherBoss.class); 
        mobTargets.add(Dolphin.class);
        mobTargets.add(IronGolem.class);
        mobTargets.add(SnowGolem.class);
        mobTargets.add(Wolf.class); 
        
        // --- Populate the Animal List ---
        mobTargets.add(Pig.class);
        mobTargets.add(Cow.class);
        mobTargets.add(Sheep.class); 
        mobTargets.add(Chicken.class);
        mobTargets.add(Rabbit.class);
        mobTargets.add(Villager.class); 
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.level == null || mc.player == null) return;

        for (Entity entity : mc.level.entitiesForRendering()) {
            
            if (entity == null || entity == mc.player) continue;
            
            if (entity instanceof Player) {
                if (players.getValue()) {
                    drawEntityESP(event, entity);
                }
                continue;
            }

            if (entity instanceof LivingEntity) {
                boolean isHostile = isHostile(entity);
                boolean isAnimal = !isHostile; 

                if (hostile.getValue() && isHostile) {
                    drawEntityESP(event, entity);
                    continue;
                }
                
                if (animals.getValue() && isAnimal) {
                    drawEntityESP(event, entity);
                }
            }
        }
    }

    private void drawEntityESP(Render3DEvent event, Entity entity) {
        AABB box = entity.getBoundingBox();

        double x = box.minX;
        double y = box.minY;
        double z = box.minZ;
        double x1 = box.maxX;
        double y1 = box.maxY;
        double z1 = box.maxZ;
        
        AABB renderBox = new AABB(x, y, z, x1, y1, z1);

        Mode mode = highlightMode.getValue();

        if (mode == Mode.Box) {
            RenderUtil.drawBoxFilled(event.getMatrix(), renderBox, boxColor.getValue());
            RenderUtil.drawBox(event.getMatrix(), renderBox, boxColor.getValue(), 2.0f);
        } else if (mode == Mode.Outline) {
            RenderUtil.drawBox(event.getMatrix(), renderBox, outlineColor.getValue(), lineWidth.getValue());
        }
    }
    
    private boolean isHostile(Entity entity) {
        return entity instanceof Monster || entity instanceof Enemy; 
    }

    public enum Mode {
        Box,
        Outline
    }
}