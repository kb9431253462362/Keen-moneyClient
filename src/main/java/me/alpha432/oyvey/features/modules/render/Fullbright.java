package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class Fullbright extends Module {

    private final Setting<Mode> mode = register(enumSetting(Mode.class).name("Mode").defaultValue(Mode.Gamma).build());

    private float originalGamma = -1.0f;

    public Fullbright() {
        super("Fullbright", "Makes the world brighter.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        // Save the original gamma setting
        originalGamma = mc.options.gamma;

        if (mode.getValue() == Mode.Gamma) {
            // Set gamma to maximum (1000.0 is often used to ensure max brightness)
            mc.options.gamma = 1000.0f; 
        } 
        // Note: The second mode (e.g., Potion) would require a separate event hook
        // and is more complex, so we rely solely on the Gamma setting for simplicity.
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;

        // Restore the original gamma setting when the module is disabled
        if (originalGamma != -1.0f) {
            mc.options.gamma = originalGamma;
        }
        originalGamma = -1.0f;
    }

    @Override
    public void onTick() {
        // We ensure the setting stays at max gamma every tick, in case another module
        // or the game tries to reset it.
        if (mode.getValue() == Mode.Gamma && mc.options.gamma < 1000.0f) {
            mc.options.gamma = 1000.0f;
        }
    }

    public enum Mode {
        Gamma,  // Modifies the client's gamma setting (recommended)
        // Potion // Placeholder for a more complex method (e.g., Night Vision)
    }
}
