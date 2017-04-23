package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.chat.ReceiveChatEvent;
import de.paxii.clarinet.event.events.game.RenderTickEvent;
import de.paxii.clarinet.event.events.player.PlayerSendChatMessageEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.ChatColor;
import de.paxii.clarinet.util.render.GL11Helper;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import org.lwjgl.opengl.GL11;

public class ModuleWorldEditESP extends Module {
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

        if (split.length >= 3) {
          double expandAmount = Double.parseDouble(split[1]);
          String expandDirection = split[2];

          this.expandSelection(SelectionDir.parseSelectionDir(expandDirection), expandAmount, 0.0D);
        } else if (split.length >= 4) {
          double expandAmount = Double.parseDouble(split[1]);
          double reverseAmount = Double.parseDouble(split[2]);
          String expandDirection = split[3];

          this.expandSelection(SelectionDir.parseSelectionDir(expandDirection), expandAmount, reverseAmount);
        }
      }
    }
  }

  @EventHandler
  public void onReceiveChat(ReceiveChatEvent event) {
    try {
      String message = ChatColor.stripColor(event.getChatMessage());

      if (message.startsWith("First position set to (")) {
        String[] split = message.split("\\(");

        String[] blockPositions = split[1].split(",");
        String zPos = blockPositions[2].split("\\)")[0];

        double posX = Double.parseDouble(blockPositions[0]);
        double posY = Double.parseDouble(blockPositions[1]);
        double posZ = Double.parseDouble(zPos);

        this.blockPos[0] = new BlockPos(posX, posY, posZ);
      } else if (message.startsWith("Second position set to (")) {
        String[] split = message.split("\\(");

        String[] blockPositions = split[1].split(",");
        String zPos = blockPositions[2].split("\\)")[0];

        double posX = Double.parseDouble(blockPositions[0]);
        double posY = Double.parseDouble(blockPositions[1]);
        double posZ = Double.parseDouble(zPos);

        this.blockPos[1] = new BlockPos(posX, posY, posZ);
      }
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  @EventHandler
  public void onGlobalRender(RenderTickEvent event) {
    try {
      this.drawWorldEditESP(blockPos[0], blockPos[1], 0xFF00FF);
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  private void expandSelection(SelectionDir expandDir, double expandAmount, double reverseAmount) {
    if (blockPos[0] == null || blockPos[1] == null)
      return;

    switch (expandDir) {
      case NORTH:
        if (blockPos[0].getZ() < blockPos[1].getZ()) {
          blockPos[0] = blockPos[0].north((int) expandAmount);
          blockPos[1] = blockPos[1].south((int) reverseAmount);
        } else {
          blockPos[1] = blockPos[1].north((int) expandAmount);
          blockPos[0] = blockPos[0].south((int) reverseAmount);
        }

        break;
      case SOUTH:
        if (blockPos[0].getZ() > blockPos[1].getZ()) {
          blockPos[1] = blockPos[0] = blockPos[0].south((int) expandAmount);
          blockPos[1].north((int) reverseAmount);
        } else {
          blockPos[1] = blockPos[1].south((int) expandAmount);
          blockPos[0] = blockPos[0].north((int) reverseAmount);
        }

        break;
      case WEST:
        if (blockPos[0].getX() < blockPos[1].getX()) {
          blockPos[0] = blockPos[0].west((int) expandAmount);
          blockPos[1] = blockPos[1].east((int) reverseAmount);
        } else {
          blockPos[1] = blockPos[1].west((int) expandAmount);
          blockPos[0] = blockPos[0].east((int) reverseAmount);
        }

        break;
      case EAST:
        if (blockPos[0].getX() > blockPos[1].getX()) {
          blockPos[0] = blockPos[0].east((int) expandAmount);
          blockPos[1] = blockPos[1].west((int) reverseAmount);
        } else {
          blockPos[1] = blockPos[1].east((int) expandAmount);
          blockPos[0] = blockPos[0].west((int) reverseAmount);
        }

        break;
      case DOWN:
        if (blockPos[0].getY() < blockPos[1].getY()) {
          blockPos[0] = blockPos[0].down((int) expandAmount);
          blockPos[1] = blockPos[1].up((int) reverseAmount);
        } else {
          blockPos[1] = blockPos[1].down((int) expandAmount);
          blockPos[0] = blockPos[0].up((int) reverseAmount);
        }

        break;
      case UP:
        if (blockPos[0].getY() > blockPos[1].getY()) {
          blockPos[0] = blockPos[0].up((int) expandAmount);
          blockPos[1] = blockPos[1].down((int) reverseAmount);
        } else {
          blockPos[1] = blockPos[1].up((int) expandAmount);
          blockPos[0] = blockPos[0].down((int) reverseAmount);
        }

        break;
    }
  }


  private void drawWorldEditESP(BlockPos block1, BlockPos block2, int color) {
    EntityPlayerSP ep = Wrapper.getPlayer();
    double xx = ep.lastTickPosX + (ep.posX - ep.lastTickPosX)
            * Wrapper.getTimer().renderPartialTicks;
    double yy = ep.lastTickPosY + (ep.posY - ep.lastTickPosY)
            * Wrapper.getTimer().renderPartialTicks;
    double zz = ep.lastTickPosZ + (ep.posZ - ep.lastTickPosZ)
            * Wrapper.getTimer().renderPartialTicks;

    double x = 0.0D, y = 0.0D, z = 0.0D, x1 = 0.0D, y1 = 0.0D, z1 = 0.0D, x2 = 0.0D, y2 = 0.0D, z2 = 0.0D, widthX = 0.0D, heightY = 1.0D, widthZ = 0.0D;

    if (block1 != null && block2 != null) {
      x = getSmaller(block1.getX(), block2.getX()) - xx;
      y = getSmaller(block1.getY(), block2.getY()) - yy;
      z = getSmaller(block1.getZ(), block2.getZ()) - zz;

      widthX = getBigger(block1.getX(), block2.getX())
              - getSmaller(block1.getX(), block2.getX());

      widthZ = getBigger(block1.getZ(), block2.getZ())
              - getSmaller(block1.getZ(), block2.getZ());

      widthZ += 1;
      widthX += 1;
    }

    if (block1 != null) {
      x1 = block1.getX() - xx;
      y1 = block1.getY() - yy;
      z1 = block1.getZ() - zz;
    }

    if (block2 != null) {
      x2 = block2.getX() - xx;
      y2 = block2.getY() - yy;
      z2 = block2.getZ() - zz;
    }

    GL11.glPushMatrix();

    GL11Helper.enableDefaults();

    GL11.glLineWidth(2.0F);

    if (blockPos[0] != null && blockPos[1] != null) {
      AxisAlignedBB blockBox0 = new AxisAlignedBB(x1, y1, z1, x1 + 1.0D,
              y1 + 1.0D, z1 + 1.0D);

      AxisAlignedBB blockBox1 = new AxisAlignedBB(x2, y2, z2, x2 + 1.0D,
              y2 + 1.0D, z2 + 1.0D);

      AxisAlignedBB draw = new AxisAlignedBB(x, y, z, x + widthX, y
              + heightY, z + widthZ);

      RenderGlobal.drawOutlinedBoundingBox(blockBox0, 0xFFFAC107);
      RenderGlobal.drawOutlinedBoundingBox(blockBox1, 0xFF07FA47);
      RenderGlobal.drawOutlinedBoundingBox(draw, color);
    } else if (blockPos[0] != null) {
      AxisAlignedBB blockBox0 = new AxisAlignedBB(x1, y1, z1, x1 + 1.0D,
              y1 + 1.0D, z1 + 1.0D);

      RenderGlobal.drawOutlinedBoundingBox(blockBox0, 0xFFFAC107);
    } else if (blockPos[1] != null) {
      AxisAlignedBB blockBox1 = new AxisAlignedBB(x2, y2, z2, x2 + 1.0D,
              y2 + 1.0D, z2 + 1.0D);
      RenderGlobal.drawOutlinedBoundingBox(blockBox1, 0xFF07FA47);
    }

    GL11Helper.disableDefaults();

    GL11.glPopMatrix();
  }

  private int getBigger(int par1, int par2) {
    if (par1 > par2)
      return par1;
    else
      return par2;
  }

  private int getSmaller(int par1, int par2) {
    if (par1 < par2)
      return par1;
    else
      return par2;
  }

  private enum SelectionDir {
    NORTH("NORTH"), SOUTH("SOUTH"), EAST("EAST"), WEST("WEST"), UP("UP"), DOWN("DOWN");

    private final String selectionName;

    SelectionDir(String selectionName) {
      this.selectionName = selectionName;
    }

    public static SelectionDir parseSelectionDir(String selection) {
      selection = selection.toUpperCase();

      for (SelectionDir selectionDir : SelectionDir.values()) {
        if (selectionDir.getName().equals(selection)) {
          return selectionDir;
        }
      }

      return null;
    }

    public String getName() {
      return this.selectionName;
    }
  }
}
