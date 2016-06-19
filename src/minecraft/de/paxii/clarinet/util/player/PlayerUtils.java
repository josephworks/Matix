package de.paxii.clarinet.util.player;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class PlayerUtils {
	public static float getPlayerStrVsBlock(Block block, ItemStack itemStack) {
		float var2 = 1.0F;

		if (itemStack != null && block != null) {
			var2 *= itemStack.getStrVsBlock(block.getBlockState().getBaseState());
		}

		return var2;
	}
}
