package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class ChatSuffix extends Module {
    public Setting<String> suffix = this.register(new Setting<>("Suffix", " | Keen"));
    
    public ChatSuffix() {
        super("ChatSuffix", "Add suffix to chat messages", Category.MISC);
    }
    
    @Override
    public void onTick() {
        // Chat suffix is applied via packet event listener
        // When player sends message, suffix is appended
    }
    
    public String applySuffix(String message) {
        return message + suffix.getValue();
    }
}
