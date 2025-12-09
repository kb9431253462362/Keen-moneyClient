package me.alpha432.oyvey.features.modules.movement;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.phys.Vec3;

public class ClickTP extends Module {
    public Setting<Double> range = this.register(new Setting<>("Range", 100.0, 10.0, 1000.0));
    
    public ClickTP() {
        super("ClickTP", "Teleport forward by clicking", Category.MOVEMENT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        // Teleport forward when enabled
        Vec3 forward = mc.player.getLookAngle().scale(range.getValue() / 20.0);
        mc.player.teleportTo(mc.player.getX() + forward.x, mc.player.getY(), mc.player.getZ() + forward.z);
    }
}
