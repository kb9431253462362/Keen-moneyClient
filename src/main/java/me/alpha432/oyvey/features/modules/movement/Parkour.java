package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

import net.minecraft.entity.Entity; // Keep this, but ensure your environment resolves it correctly
import net.minecraft.util.shape.VoxelShapes; // Needed for modern collision checks

public class Parkour extends Module{

	public Parkour() {
		super("Parkour", "Auto Parkour", Category.MOVEMENT);
	}
	
	@Override // Added @Override for clarity
	public void onUpdate() {
		if(this.isEnabled()) { // FIX: Use isEnabled()
			if(mc.player.isOnGround() // Check if on ground
			    && !mc.player.isSneaking() // Check if not sneaking
			    && !this.mc.options.sneakKey.isPressed() 
	
			    && mc.world.isAir(mc.player.getBlockPos().down())) 
			{
		
			    mc.player.jump();
			}
		}
	}

}
