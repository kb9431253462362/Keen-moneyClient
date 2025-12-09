package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.chat.Component;

public class FakeChat extends Module {
    public Setting<String> message = this.register(new Setting<>("Message", "Hello!"));
    private int messageTimer = 0;
    
    public FakeChat() {
        super("FakeChat", "Send local fake chat messages", Category.MISC);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        messageTimer++;
        if (messageTimer >= 100) {
            messageTimer = 0;
            
            // Send fake message locally (doesn't go to server)
            if (mc.gui != null) {
                mc.gui.getChat().addMessage(Component.literal("[Fake] " + message.getValue()));
            }
        }
    }
}
