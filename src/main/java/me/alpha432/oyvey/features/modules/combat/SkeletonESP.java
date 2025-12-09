package me.alpha432.oyvey.features.modules.combat;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class SkeletonESP extends Module {
    public Setting<Boolean> showHealth = this.register(new Setting<>("ShowHealth", true));
    public SkeletonESP() {
        super("SkeletonESP", "ESP for skeleton players", Category.COMBAT);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        // Track skeleton entities
        mc.level.getEntities(null, mc.player.getBoundingBox().inflate(50)).forEach(entity -> {
            // Check if entity is skeleton
            if (entity instanceof net.minecraft.world.entity.monster.Skeleton) {
                // Would highlight in render event
                if (showHealth.getValue()) {
                    // Track health for display
                }
            }
        });
    }
}
