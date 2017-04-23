package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.chat.ReceiveChatEvent;
import de.paxii.clarinet.event.events.game.RenderTickEvent;
import de.paxii.clarinet.event.events.player.PlayerSendChatMessageEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.ChatColor;
import de.paxii.clarinet.util.function.TwoBooleansFunction;
import de.paxii.clarinet.util.render.GL11Helper;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import org.lwjgl.opengl.GL11;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleWorldEditESP extends Module {
  private static final Pattern worldEditPattern = Pattern.compile("(First|Second) position set to \\((-?\\d+\\.\\d), (-?\\d+\\.\\d), (-?\\d+\\.\\d)\\).*");
  private final BlockPos[] blockPos = {null, null};

  public ModuleWorldEditESP() {
    super("WorldEditESP", ModuleCategory.RENDER, -1);

    this.setRegistered(true);
    this.setDescription("Draws a box around your current worldedit selection.");
  }

  @EventHandler
  public void onChatMessage(PlayerSendChatMessageEvent event) {
    if (event.getChatMessage().startsWith("//")) {
      String weCommand = event.getChatMessage().substring(2);

      if (weCommand.startsWith("desel") || weCommand.startsWith("sel")) {
        this.blockPos[0] = null;
        this.blockPos[1] = null;
      }

      if (weCommand.startsWith("expand")) {
        String[] split = weCommand.split(" ");

        String expandDirection = "";
        double expandAmount = 0, reverseAmount = 0;

        try {
          if (split.length == 2) {
            expandDirection = split[1];

            if (expandDirection.equalsIgnoreCase("vert")) {
              expandDirection = "up";
              expandAmount = 256;
              reverseAmount = 256;
            }
          } else if (split.length == 3) {
            expandAmount = Double.parseDouble(split[1]);
            expandDirection = split[2];
          } else if (split.length > 3) {
            expandAmount = Double.parseDouble(split[1]);
            reverseAmount = Double.parseDouble(split[2]);
            expandDirection = split[3];
          }
        } catch (NumberFormatException exception) {
          this.sendClientMessage("Invalid expand syntax detected.");
        }

        if (expandDirection.length() != 0 && expandAmount != 0) {
          EnumFacing enumFacing = EnumFacing.byName(expandDirection);
          if (enumFacing == null) {
            this.sendClientMessage("Could not determine expand direction. Please use full words (north, east, vert is fine though)");
            return;
          }
          this.expandSelection(enumFacing, (int) expandAmount, (int) reverseAmount);
        }
      }
    }
  }

  @EventHandler
  public void onReceiveChat(ReceiveChatEvent event) {
    String message = ChatColor.stripColor(event.getChatMessage());
    Matcher matcher = worldEditPattern.matcher(message);

    if (matcher.find()) {
      int index = matcher.group(1).equals("First") ? 0 : 1;
      double posX = Double.parseDouble(matcher.group(2));
      double posY = Double.parseDouble(matcher.group(3));
      double posZ = Double.parseDouble(matcher.group(4));

      this.blockPos[index] = new BlockPos(posX, posY, posZ);
    }
  }

  @EventHandler
  public void onGlobalRender(RenderTickEvent event) {
    BlockPos block1 = this.blockPos[0];
    BlockPos block2 = this.blockPos[1];
    EntityPlayerSP entityPlayer = Wrapper.getPlayer();

    float partialTicks = Wrapper.getTimer().renderPartialTicks;
    double positionX = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * partialTicks;
    double positionY = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * partialTicks;
    double positionZ = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * partialTicks;

    GL11.glPushMatrix();
    GL11Helper.enableDefaults();
    GL11.glLineWidth(1.5F);

    if (block1 != null) {
      double renderX = block1.getX() - positionX;
      double renderY = block1.getY() - positionY;
      double renderZ = block1.getZ() - positionZ;

      AxisAlignedBB blockBox0 = new AxisAlignedBB(renderX, renderY, renderZ, renderX + 1.0D, renderY + 1.0D, renderZ + 1.0D);
      RenderGlobal.drawOutlinedBoundingBox(blockBox0, 0xFFFAC107);
    }

    if (block2 != null) {
      double renderX = block2.getX() - positionX;
      double renderY = block2.getY() - positionY;
      double renderZ = block2.getZ() - positionZ;

      AxisAlignedBB blockBox1 = new AxisAlignedBB(renderX, renderY, renderZ, renderX + 1.0D, renderY + 1.0D, renderZ + 1.0D);
      RenderGlobal.drawOutlinedBoundingBox(blockBox1, 0xFF07FA47);
    }

    if (block1 != null && block2 != null) {
      double renderX = Math.min(block1.getX(), block2.getX()) - positionX;
      double renderY = Math.min(block1.getY(), block2.getY()) - positionY;
      double renderZ = Math.min(block1.getZ(), block2.getZ()) - positionZ;

      double deltaX = Math.max(block1.getX(), block2.getX()) - Math.min(block1.getX(), block2.getX()) + 1;
      double deltaY = Math.max(block1.getY(), block2.getY()) - Math.min(block1.getY(), block2.getY()) + 1;
      double deltaZ = Math.max(block1.getZ(), block2.getZ()) - Math.min(block1.getZ(), block2.getZ()) + 1;

      AxisAlignedBB draw = new AxisAlignedBB(renderX, renderY, renderZ, renderX + deltaX, renderY + deltaY, renderZ + deltaZ);
      RenderGlobal.drawOutlinedBoundingBox(draw, 0xFF00FF);
    }

    GL11Helper.disableDefaults();
    GL11.glPopMatrix();
  }

  /**
   * Expand the current WorldEdit Selection into a given direction
   *
   * @param expandDir     EnumFacing to expand the selection into
   * @param expandAmount  int amount of blocks to expand in the expandDir
   * @param reverseAmount int amount of blocks to expand in the oppsite direction of expandDir
   */
  private void expandSelection(EnumFacing expandDir, int expandAmount, int reverseAmount) {
    if (this.blockPos[0] == null || this.blockPos[1] == null)
      return;

    boolean blockCondition = true;
    boolean reverseCondition = false;
    TwoBooleansFunction<Integer> findIndex = (condition, reverse) -> condition ? reverse ? 1 : 0 : reverse ? 0 : 1;

    if (expandDir.getFrontOffsetX() != 0) {
      blockCondition = this.blockPos[0].getX() >= this.blockPos[1].getX();
      reverseCondition = expandDir.getFrontOffsetX() < 0;
    } else if (expandDir.getFrontOffsetY() != 0) {
      blockCondition = this.blockPos[0].getY() >= this.blockPos[1].getY();
      reverseCondition = expandDir.getFrontOffsetY() < 0;
    } else if (expandDir.getFrontOffsetZ() != 0) {
      blockCondition = this.blockPos[0].getZ() >= this.blockPos[1].getZ();
      reverseCondition = expandDir.getFrontOffsetZ() < 0;
    }
    int blockIndex = findIndex.apply(blockCondition, reverseCondition);
    int reverseIndex = blockIndex == 0 ? 1 : 0;

    BlockPos firstBlock = this.blockPos[blockIndex].offset(expandDir, expandAmount);
    BlockPos secondBlock = this.blockPos[reverseIndex].offset(expandDir.getOpposite(), reverseAmount);

    this.blockPos[blockIndex] = this.getCorrectedBlockPos(firstBlock);
    this.blockPos[reverseIndex] = this.getCorrectedBlockPos(secondBlock);
  }

  /**
   * Corrects a given BlockPos (checks world bounds)
   *
   * @param blockPos BlockPos to correct
   * @return the corrected BlockPos
   */
  private BlockPos getCorrectedBlockPos(BlockPos blockPos) {
    if (blockPos.getY() < 0) {
      blockPos = blockPos.down(blockPos.getY());
    }
    if (blockPos.getY() > Wrapper.getWorld().getHeight()) {
      blockPos = blockPos.down(blockPos.getY() - Wrapper.getWorld().getHeight());
    }

    return blockPos;
  }

}
