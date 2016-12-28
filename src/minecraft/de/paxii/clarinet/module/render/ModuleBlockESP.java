package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.block.BlockBreakEvent;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.event.events.game.LoadWorldEvent;
import de.paxii.clarinet.event.events.game.QuitServerEvent;
import de.paxii.clarinet.event.events.game.RenderTickEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.render.GL11Helper;
import de.paxii.clarinet.util.settings.type.ClientSettingInteger;
import de.paxii.clarinet.util.threads.ConcurrentArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import org.lwjgl.opengl.GL11;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

public class ModuleBlockESP extends Module {
  private final ConcurrentHashMap<Integer, Integer> searchBlocks;
  private ConcurrentArrayList<SearchBlock> renderBlocks;
  private BlockPos updatePosition;

  public ModuleBlockESP() {
    super("BlockESP", ModuleCategory.RENDER);

    this.getModuleValues().put("renderDistance", new ValueBase("Block ESP Distance", 50, 1, 500, true, "Distance") {
      @Override
      public void onUpdate(float oldValue, float newValue) {
        ModuleBlockESP.this.renderBlocks.clear();
        ModuleBlockESP.this.searchBlocks();
      }
    });

    this.searchBlocks = new ConcurrentHashMap<>();
    this.renderBlocks = new ConcurrentArrayList<>();

    this.setCommand(true);
    this.setRegistered(true);
    this.setDescription("Draws a Box around Blocks");
    this.setSyntax("blockesp <add/remove/list> <blockID> <color (0xFFFFFF)>");
  }

  @Override
  public void onStartup() {
    this.getModuleSettings().forEach((blockIdentifier, value) -> {
      try {
        int blockID = Integer.parseInt(blockIdentifier);
        int blockColor = this.getValue(blockIdentifier, Integer.class);

        this.searchBlocks.put(blockID, blockColor);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    });
  }

  @EventHandler
  public void onTick(IngameTickEvent event) {
    this.updateRenderBlocks();
  }

  @EventHandler
  public void onWorldLoaded(LoadWorldEvent event) {
    this.updatePosition = null;
    this.updateRenderBlocks();
  }

  @EventHandler
  public void onRender(RenderTickEvent event) {
    for (SearchBlock renderBlock : this.renderBlocks) {
      int renderColor = searchBlocks.get(renderBlock.getBlockID());

      BlockPos blockPos = renderBlock.getBlockPos();

      double xx = Wrapper.getPlayer().lastTickPosX
              + (Wrapper.getPlayer().posX - Wrapper.getPlayer().lastTickPosX)
              * Wrapper.getTimer().renderPartialTicks;
      double yy = Wrapper.getPlayer().lastTickPosY
              + (Wrapper.getPlayer().posY - Wrapper.getPlayer().lastTickPosY)
              * Wrapper.getTimer().renderPartialTicks;
      double zz = Wrapper.getPlayer().lastTickPosZ
              + (Wrapper.getPlayer().posZ - Wrapper.getPlayer().lastTickPosZ)
              * Wrapper.getTimer().renderPartialTicks;

      double x = blockPos.getX() - xx;
      double y = blockPos.getY() - yy;
      double z = blockPos.getZ() - zz;
      AxisAlignedBB blockBox = new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);

      GL11.glPushMatrix();
      GL11Helper.enableDefaults();
      GL11.glEnable(GL11.GL_LINE_SMOOTH);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glDepthMask(false);
      GlStateManager.depthMask(false);
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.disableBlend();
      GL11.glLineWidth(1.0F);
      RenderGlobal.drawOutlinedBoundingBox(blockBox, renderColor);

      GL11Helper.disableDefaults();
      GlStateManager.enableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
      GL11.glDepthMask(true);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_LINE_SMOOTH);
      GL11.glPopMatrix();
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
    int blockID = Block.getIdFromBlock(blockBreakEvent.getBlock());

    if (this.searchBlocks.containsKey(blockID)) {
      this.renderBlocks.forEach((searchBlock -> {
        if (searchBlock.getBlockPos().equals(blockBreakEvent.getBlockPos())) {
          this.renderBlocks.remove(searchBlock);
        }
      }));
    }
  }

  private void updateRenderBlocks() {
    if (this.updatePosition == null) {
      this.renderBlocks.clear();
      this.searchBlocks();

      this.updatePosition = Wrapper.getPlayer().getPosition();
    } else {
      double distance = Wrapper.getPlayer().getDistance(this.updatePosition.getX(), this.updatePosition.getY(), this.updatePosition.getZ());

      if (distance > this.getModuleValues().get("renderDistance").getValue() / 5) {
        this.renderBlocks.clear();
        this.searchBlocks();

        this.updatePosition = Wrapper.getPlayer().getPosition();
      }
    }
  }

  private void searchBlocks() {
    if (!this.searchBlocks.isEmpty()) {
      new Thread(() -> {
        int renderDistance = (int) (this.getModuleValues().get("renderDistance")).getValue();

        for (int y = 0; y < 256; y++) {
          for (int x = 0; x < renderDistance; x++) {
            for (int z = 0; z < renderDistance; z++) {
              int blockX = (int) Wrapper.getPlayer().posX
                      - renderDistance / 2 + x;
              int blockZ = (int) Wrapper.getPlayer().posZ
                      - renderDistance / 2 + z;

              BlockPos blockPos = new BlockPos(blockX, y, blockZ);

              Block block = Wrapper.getWorld()
                      .getBlockState(blockPos).getBlock();

              int blockID = Block.getIdFromBlock(block);

              if (this.searchBlocks.containsKey(blockID)) {
                this.renderBlocks.add(new SearchBlock(blockID, blockPos));
              }
            }
          }
        }
      }).start();
    }
  }

  @Override
  public void onCommand(String[] args) {
    if (args.length > 0) {
      if (args.length >= 2) {
        String identifier = args[0];
        int blockID;
        int blockColor = 0xFFFFFF;

        try {
          blockID = Integer.parseInt(args[1]);
        } catch (Exception e) {
          Chat.printClientMessage("Invalid BlockID!");
          return;
        }

        if (identifier.equalsIgnoreCase("add")) {
          if (args.length >= 3) {
            try {
              String colorString = args[2];
              if (colorString.startsWith("0x")) {
                colorString = colorString.substring(2);
              }

              blockColor = Integer.parseInt(colorString, 16);
            } catch (Exception e) {
              e.printStackTrace();
              Chat.printClientMessage("Invalid Block Color!");
              return;
            }
          }

          if (!this.searchBlocks.containsKey(blockID)) {
            this.searchBlocks.put(blockID, blockColor);
            this.renderBlocks.clear();
            this.searchBlocks();

            Chat.printClientMessage("Block " + blockID + " (0x" + Integer.toHexString(blockColor) + ") has been added.");
          } else {
            Chat.printClientMessage("Block " + blockID + " has already been added!");
          }
        } else if (identifier.equalsIgnoreCase("remove")) {
          if (this.searchBlocks.containsKey(blockID)) {
            this.searchBlocks.remove(blockID);
            this.renderBlocks.clear();
            this.searchBlocks();

            Chat.printClientMessage("Block " + blockID + " has been removed.");
          } else {
            Chat.printClientMessage("Block " + blockID + " hasn't been added yet!");
          }
        } else {
          Chat.printClientMessage("Unknown subcommand!");
        }
      } else if (args.length >= 1) {
        if (args[0].equalsIgnoreCase("list")) {
          Chat.printClientMessage("Block ESP List:");
          for (Entry<Integer, Integer> searchBlock : searchBlocks.entrySet()) {
            Chat.printClientMessage("Block: " + searchBlock.getKey() + " (0x" + Integer.toHexString(searchBlock.getValue()) + ")");
          }
        } else if (args[0].equalsIgnoreCase("clear")) {
          this.searchBlocks.clear();
          this.renderBlocks.clear();
          this.searchBlocks();
          Chat.printClientMessage("Block ESP list cleared.");
        } else {
          Chat.printClientMessage("Unknown subcommand!");
        }
      }
    } else {
      Chat.printClientMessage("Too few arguments!");
    }
  }

  @EventHandler
  public void onServerQuit(QuitServerEvent quitServerEvent) {
    this.setEnabled(false);
  }

  @Override
  public void onDisable() {
    this.renderBlocks.clear();
    this.updatePosition = null;
  }

  @Override
  public void onShutdown() {
    this.getModuleSettings().clear();
    for (Entry<Integer, Integer> searchBlock : this.searchBlocks.entrySet()) {
      this.getModuleSettings().put(String.valueOf(searchBlock.getKey()), new ClientSettingInteger(String.valueOf(searchBlock.getKey()), searchBlock.getValue()));
    }
  }

  private class SearchBlock {
    @Getter
    private int blockID;
    @Getter
    private BlockPos blockPos;

    SearchBlock(int blockID, BlockPos blockPos) {
      this.blockID = blockID;
      this.blockPos = blockPos;
    }
  }
}
