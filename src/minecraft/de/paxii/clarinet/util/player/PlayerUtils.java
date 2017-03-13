package de.paxii.clarinet.util.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.chat.ChatColor;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerUtils {
  public static float getPlayerStrVsBlock(Block block, ItemStack itemStack) {
    float var2 = 1.0F;

    if (itemStack != null && block != null) {
      var2 *= itemStack.getStrVsBlock(block.getBlockState().getBaseState());
    }

    return var2;
  }

  public static EntityPlayer getPlayerByName(String playerName) {
    playerName = ChatColor.stripColor(playerName);

    for (EntityPlayer entityPlayer : Wrapper.getWorld().playerEntities) {
      if (ChatColor.stripColor(entityPlayer.getName()).equals(playerName)) {
        return entityPlayer;
      }
    }

    return null;
  }
}
