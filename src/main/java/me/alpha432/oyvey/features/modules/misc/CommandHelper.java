package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;

public class CommandHelper extends Module {
    public Setting<String> prefix = this.register(new Setting<>("Prefix", "."));
    
    public CommandHelper() {
        super("CommandHelper", "Autocomplete commands", Category.MISC);
    }
    
    @Override
    public void onTick() {
        // Command autocomplete is handled via screen events
        // Shows available commands as you type
    }
    
    public boolean isCommand(String text) {
        return text.startsWith(prefix.getValue());
    }
}
