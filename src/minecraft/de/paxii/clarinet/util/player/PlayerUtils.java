package de.paxii.clarinet.util.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.chat.ChatColor;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

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

  public static boolean canEntityBeSeen(Entity entityA, Entity entityB) {
    RayTraceResult traceResult = Wrapper.getWorld().rayTraceBlocks(
            new Vec3d(entityA.posX, entityA.posY + (double) entityA.getEyeHeight(), entityA.posZ),
            new Vec3d(entityB.posX, entityB.posY, entityB.posZ),
            false,
            true,
            false);
    if (traceResult != null) {
      if (traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
        Block block = Wrapper.getWorld().getBlockState(traceResult.getBlockPos()).getBlock();
        return block.getBlockState().getProperties().stream().filter(property -> property.getName().equals("half")).count() == 1;
      }
    }

    return traceResult == null;
  }
}
