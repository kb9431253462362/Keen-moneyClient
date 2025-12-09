package me.alpha432.oyvey.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.client.Notifications;
import me.alpha432.oyvey.features.modules.hud.Coordinates; // Added for completeness
import me.alpha432.oyvey.features.modules.hud.Watermark; // Added for completeness
import me.alpha432.oyvey.features.modules.client.HudEditor;
import me.alpha432.oyvey.features.modules.combat.Criticals;
import me.alpha432.oyvey.features.modules.misc.MCF;
import me.alpha432.oyvey.features.modules.movement.ReverseStep;
import me.alpha432.oyvey.features.modules.movement.Step;
import me.alpha432.oyvey.features.modules.player.FastPlace;
import me.alpha432.oyvey.features.modules.player.NoFall;
import me.alpha432.oyvey.features.modules.player.Velocity;
import me.alpha432.oyvey.features.modules.render.BlockHighlight;
import me.alpha432.oyvey.features.modules.render.BlockESP;
import me.alpha432.oyvey.features.modules.render.ESP;
import me.alpha432.oyvey.features.modules.render.Xray;
import me.alpha432.oyvey.features.modules.combat.AutoClicker;
import me.alpha432.oyvey.features.modules.combat.TriggerBot;
import me.alpha432.oyvey.features.modules.combat.AimAssist;
import me.alpha432.oyvey.features.modules.combat.HitBox;
import me.alpha432.oyvey.features.modules.combat.AutoSwitch;
import me.alpha432.oyvey.util.traits.Jsonable;
import me.alpha432.oyvey.util.traits.Util;
import me.alpha432.oyvey.features.modules.movement.AirJump;
import me.alpha432.oyvey.features.modules.movement.Speed;
import me.alpha432.oyvey.features.modules.movement.Spider;
import me.alpha432.oyvey.features.modules.movement.Jesus;
import me.alpha432.oyvey.features.modules.movement.Flight;
import me.alpha432.oyvey.features.modules.movement.SafeWalk;
import me.alpha432.oyvey.features.modules.player.FastThrowXP;

import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.features.modules.combat.Reach;
import me.alpha432.oyvey.features.modules.combat.AntiKnockback;
import me.alpha432.oyvey.features.modules.combat.SmartCrits;
import me.alpha432.oyvey.features.modules.combat.CrystalAura;
import me.alpha432.oyvey.features.modules.combat.AutoAnchor;
import me.alpha432.oyvey.features.modules.movement.LongJump;
import me.alpha432.oyvey.features.modules.movement.StrafeAssist;
import me.alpha432.oyvey.features.modules.player.AutoTotem;
import me.alpha432.oyvey.features.modules.player.NoHunger;
import me.alpha432.oyvey.features.modules.player.FastEat;
import me.alpha432.oyvey.features.modules.player.AutoFish;
import me.alpha432.oyvey.features.modules.player.Sprint;
import me.alpha432.oyvey.features.modules.render.Tracers;
import me.alpha432.oyvey.features.modules.render.Nametags;
import me.alpha432.oyvey.features.modules.combat.BedAura;
import me.alpha432.oyvey.features.modules.combat.SelfTrap;
import me.alpha432.oyvey.features.modules.combat.BowAimbot;
import me.alpha432.oyvey.features.modules.combat.Surround;
import me.alpha432.oyvey.features.modules.combat.AutoWeb;
import me.alpha432.oyvey.features.modules.combat.ArrowDodge;
import me.alpha432.oyvey.features.modules.combat.SelfWeb;
import me.alpha432.oyvey.features.modules.combat.AntiBot;
import me.alpha432.oyvey.features.modules.movement.TridentBoost;
import me.alpha432.oyvey.features.modules.movement.Strafe;
import me.alpha432.oyvey.features.modules.movement.Anchor;

import me.alpha432.oyvey.features.modules.movement.HighJump;
import me.alpha432.oyvey.features.modules.movement.ClickTP;
import me.alpha432.oyvey.features.modules.movement.ElytraBoost;
import me.alpha432.oyvey.features.modules.movement.ElytraFly;
import me.alpha432.oyvey.features.modules.movement.BoatFly;
import me.alpha432.oyvey.features.modules.combat.AutoEXP;

import me.alpha432.oyvey.features.modules.player.FastUse;
import me.alpha432.oyvey.features.modules.player.AntiAFK;
import me.alpha432.oyvey.features.modules.player.GhostHand;
import me.alpha432.oyvey.features.modules.render.Freecam;
import me.alpha432.oyvey.features.modules.render.Fullbright;
import me.alpha432.oyvey.features.modules.render.NoRender;
import me.alpha432.oyvey.features.modules.render.ItemPhysics;
import me.alpha432.oyvey.features.modules.render.Chams;
import me.alpha432.oyvey.features.modules.render.Zoom;
import me.alpha432.oyvey.features.modules.render.Trail;
import me.alpha432.oyvey.features.modules.render.PopChams;
import me.alpha432.oyvey.features.modules.render.LogoutSpots;
import me.alpha432.oyvey.features.modules.render.StorageESP;
import me.alpha432.oyvey.features.modules.render.CityESP;
import me.alpha432.oyvey.features.modules.render.HoleESP;
import me.alpha432.oyvey.features.modules.render.LightOverlay;
import me.alpha432.oyvey.features.modules.render.Marker;
import me.alpha432.oyvey.features.modules.misc.ChatSuffix;
import me.alpha432.oyvey.features.modules.misc.AutoRespawn;
import me.alpha432.oyvey.features.modules.misc.Timer;
import me.alpha432.oyvey.features.modules.misc.FakePlayer;
import me.alpha432.oyvey.features.modules.misc.FakeChat;
import me.alpha432.oyvey.features.modules.misc.CommandHelper;
import me.alpha432.oyvey.features.modules.misc.BedwarsHelper;
import me.alpha432.oyvey.features.modules.misc.AutoPot;

import java.util.*;
import java.util.stream.Stream;

public class ModuleManager implements Jsonable, Util {
    private final Map<Class<? extends Module>, Module> fastRegistry = new HashMap<>();
    private final List<Module> modules = new ArrayList<>();

  public void init() {
    // --- HUD Modules ---
    register(new Watermark());
    register(new Coordinates());
    register(new HudEditor());
    
    // --- Client Modules ---
    register(new ClickGui());
    register(new Notifications());
    
    // --- Combat Modules ---
    register(new Criticals());
    register(new Killaura());
    register(new Reach());
    register(new AutoClicker());
    register(new TriggerBot());
    register(new AimAssist());
    register(new HitBox());
    register(new AutoSwitch());
    register(new AntiKnockback());
    register(new SmartCrits());
    register(new CrystalAura());
    register(new AutoAnchor());
    register(new BedAura());
    register(new SelfTrap());
    register(new BowAimbot());
    register(new Surround());
    register(new AutoWeb());
    register(new ArrowDodge());
    register(new SelfWeb());
    register(new AntiBot());
    register(new AutoEXP());


    
    // --- Movement Modules ---
    register(new Step());
    register(new ReverseStep());
    register(new Speed());
    register(new Spider());
    register(new Jesus());
    register(new AirJump());
    register(new Flight());
    register(new SafeWalk());
    register(new LongJump());
    register(new StrafeAssist());
    register(new TridentBoost());
    register(new Strafe());
    register(new Anchor());
   
    register(new HighJump());
    register(new ClickTP());
    register(new ElytraBoost());
    register(new ElytraFly());
    register(new BoatFly());


    // --- Player Modules ---
    register(new FastPlace());
    register(new Velocity());
    register(new NoFall());
    register(new FastThrowXP());
    register(new AutoTotem());
    register(new NoHunger());
    register(new FastEat());
    register(new AutoFish());
    register(new Sprint());
    register(new FastUse());
    register(new AntiAFK());
    register(new GhostHand());
    

    // --- Misc Modules ---
    register(new MCF());
    register(new ChatSuffix());
    register(new AutoRespawn());
    register(new Timer());
    register(new FakePlayer());
    register(new FakeChat());
    register(new CommandHelper());
    register(new BedwarsHelper());
    register(new AutoPot());
    

    // --- Render Modules ---
    register(new BlockHighlight());
    register(new BlockESP());
    register(new ESP());
    register(new Xray());
    register(new Tracers());
    register(new Nametags());
    register(new Freecam());
    register(new Fullbright());
    register(new NoRender());
    register(new ItemPhysics());
    register(new Chams());
    register(new Zoom());
    register(new Trail());
    register(new PopChams());
    register(new LogoutSpots());
    register(new StorageESP());
    register(new CityESP());
    register(new HoleESP());
    register(new LightOverlay());
    register(new Marker());
    register(new LogoutSpots());
    register(new StorageESP());
    register(new CityESP());
    register(new HoleESP());
    register(new LightOverlay());
    register(new Marker());
    register(new Trail());
    
  }
    public void register(Module module) {
        getModules().add(module);
        fastRegistry.put(module.getClass(), module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Stream<Module> stream() {
        return getModules().stream();
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        return (T) fastRegistry.get(clazz);
    }

    public Module getModuleByName(String name) {
        return stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Module getModuleByDisplayName(String display) {
        return stream().filter(m -> m.getDisplayName().equalsIgnoreCase(display)).findFirst().orElse(null);
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        return stream().filter(m -> m.getCategory() == category).toList();
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        getModules().forEach(Module::onLoad);
    }

    public void onTick() {
        stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void onUnload() {
        getModules().forEach(EVENT_BUS::unregister);
        getModules().forEach(Module::onUnload);
    }

    public void onKeyPressed(int key) {
        if (key <= 0 || mc.screen != null) return;
        stream().filter(module -> module.getBind().getKey() == key).forEach(Module::toggle);
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Module module : getModules()) {
            object.add(module.getName(), module.toJson());
        }
        return object;
    }

    @Override
    public void fromJson(JsonElement element) {
        for (Module module : getModules()) {
            module.fromJson(element.getAsJsonObject().get(module.getName()));
        }
    }

    @Override
    public String getFileName() {
        return "modules.json";
    }
}
