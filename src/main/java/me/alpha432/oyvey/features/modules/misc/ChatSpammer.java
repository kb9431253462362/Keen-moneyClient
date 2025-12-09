package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;


import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.client.MinecraftClient;


 
public class ChatSpammer extends Module {

  
    private final Setting<String> message = register(new Setting<>("Message", "Keen-moneyClient ON TOP!"));
    private final Setting<Integer> delay = register(new Setting<>("Delay", 10, 1, 100)); 
    private final Setting<Boolean> antiGrief = register(new Setting<>("AntiGrief", true, "Adds random chars to bypass simple anti-spam."));
    

    private int spamTimer = 0;

    public ChatSpammer() {
        super("ChatSpammer", "Spams a custom message in chat.", Category.MISC);
    }

    @Override
    public void onUpdate() {
        if (!isEnabled() || mc.player == null) {
            return;
        }

      
        if (spamTimer >= delay.getValue()) {
            
       
            String finalMessage = message.getValue();

        
            if (antiGrief.getValue()) {
                finalMessage += " " + getRandomAntiSpamSuffix();
            }

        
            mc.player.sendChatMessage(finalMessage);
         
           // if (mc.player.networkHandler != null) {
            //    mc.player.networkHandler.sendPacket(new ChatMessageC2SPacket(finalMessage));
          //  }
            

           
            spamTimer = 0;
        }

      
        spamTimer++;
    }

    private String getRandomAntiSpamSuffix() {
    
        long currentTime = System.currentTimeMillis();
        return "\u00A78" + (currentTime % 1000000); 
    }

    @Override
    public void onDisable() {
        spamTimer = 0;
    }
}
