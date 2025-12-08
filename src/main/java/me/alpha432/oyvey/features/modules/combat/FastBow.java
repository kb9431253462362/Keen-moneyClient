package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient; // Modern Minecraft Client
import net.minecraft.item.BowItem; // Modern ItemBow
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket; // C03PacketPlayer
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket; // C07PacketPlayerDigging
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action; // C07PacketPlayerDigging.Action
import net.minecraft.util.math.BlockPos; // Modern BlockPos
import net.minecraft.util.math.Direction; // Modern EnumFacing

public class FastBow extends Module{

	// FIX: Module constructor must match parent (name, description, category)
	public FastBow() {
		super("FastBow", "Fires bows instantly", Category.COMBAT); 
	}
	
	@Override
	public void onUpdate() {
		if(this.isEnabled()) { // FIX: Use isEnabled() or change the method name in Feature
			if(MinecraftClient.getInstance().player != null && // Use Modern client instance
					MinecraftClient.getInstance().player.getHealth() > 0
					&& (MinecraftClient.getInstance().player.isOnGround() || MinecraftClient.getInstance().player.getAbilities().creativeMode)
					&& MinecraftClient.getInstance().player.getInventory().getMainHandStack() != null
					&& MinecraftClient.getInstance().player.getInventory().getMainHandStack().getItem() instanceof BowItem // Modern BowItem
					&& MinecraftClient.getInstance().options.useKey.isPressed()) // Use modern key
				{
					
					// Simulate drawing the bow
					MinecraftClient.getInstance().interactionManager.interactItem(
							MinecraftClient.getInstance().player,
							MinecraftClient.getInstance().world,
							MinecraftClient.getInstance().player.getInventory().getMainHandStack()
						);
					
					// Send C03 packets to speed up the bow draw time (20 packets for instant)
					for(int i = 0; i < 20; i++)
						MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
							MinecraftClient.getInstance().player.getX(),
							MinecraftClient.getInstance().player.getY(),
							MinecraftClient.getInstance().player.getZ(),
							false // onGround, this can vary
						));
						
					// Send C07 packet to release the item
					MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerActionC2SPacket(
						Action.RELEASE_USE_ITEM, 
						BlockPos.ORIGIN, // Use a dummy BlockPos (0,0,0)
						Direction.DOWN // Use a dummy direction
					));
					
					// Stop using the item client-side
					MinecraftClient.getInstance().player.stopUsingItem();
					
					// Note: The original code used methods like onItemRightClick and onPlayerStoppedUsing 
					// which are generally not directly called in modern client mods but are simulated 
					// by the packets and client interaction manager.
				}
			}
		}
}
