package de.paxii.clarinet.module.render;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.IngameTickEvent;
import de.paxii.clarinet.gui.ingame.panel.GuiPanel;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.settings.type.ClientSettingInteger;
import de.paxii.clarinet.util.threads.ConcurrentArrayList;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * Created by Lars on 29.03.2016.
 */
public class ModuleXray extends Module {
	@Getter
	private static boolean isActive;
	@Getter
	private static ConcurrentArrayList<Integer> blockList = new ConcurrentArrayList<>();
	@Getter
	private static int xrayOpacity = 0x44FFFFFF;
	private boolean editedAmbientOcclusion;

	public ModuleXray() {
		super("Xray", ModuleCategory.RENDER, Keyboard.KEY_X);

		this.getModuleValues().put("opacity", new ValueBase("Xray Opacity", 30.0F, 0.0F, 100.0F, true) {
			@Override
			public void onUpdate(float oldValue, float newValue) {
				int opacity = (int) (newValue * 255 / 100);
				Color color = new Color(255, 255, 255, opacity);
				ModuleXray.xrayOpacity = color.getRGB();

				if (ModuleXray.this.isEnabled()) {
					Wrapper.getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});

		this.setCommand(true);
		this.setDescription("Allows you to see Blocks through walls.");
		this.setSyntax("xray <add/remove/list/gui> <blockID>");
	}

	@Override
	public void onStartup() {
		if (this.getModuleSettings().isEmpty()) {
			blockList.add(54);
			blockList.add(56);
			blockList.add(14);
			blockList.add(15);
			blockList.add(16);
			blockList.add(21);
			blockList.add(129);
		} else {
			this.getModuleSettings().forEach((blockIdentifier, value) -> {
				try {
					int blockID = this.getValue(blockIdentifier, Integer.class);

					blockList.add(blockID);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Override
	public void onEnable() {
		Wrapper.getEventManager().register(this);

		if (Wrapper.getGameSettings().ambientOcclusion == 0) {
			Wrapper.getGameSettings().ambientOcclusion = 1;
			this.editedAmbientOcclusion = true;
		}
	}

	@EventHandler
	public void onIngameTick(IngameTickEvent event) {
		if (Wrapper.getWorld().provider.getLightBrightnessTable()[0] != 1)
			for (int i = 0; 15 >= i; ++i) {
				Wrapper.getWorld().provider.getLightBrightnessTable()[i] = 1;
			}
	}

	@Override
	public void onToggle() {
		ModuleXray.isActive = !ModuleXray.isActive;
		Wrapper.getMinecraft().renderGlobal.loadRenderers();
	}

	@Override
	public void onCommand(String[] args) {
		if (args.length > 0) {
			if (args.length >= 2) {
				String identifier = args[0];
				int blockID;

				try {
					blockID = Integer.parseInt(args[1]);
				} catch (Exception e) {
					Chat.printClientMessage("Invalid BlockID!");
					return;
				}

				if (identifier.equalsIgnoreCase("add")) {
					if (!blockList.contains(blockID)) {
						blockList.add(blockID);

						Chat.printClientMessage("Block " + blockID + " has been added.");
						Wrapper.getMinecraft().renderGlobal.loadRenderers();
					} else {
						Chat.printClientMessage("Block " + blockID + " has already been added!");
					}
				} else if (identifier.equalsIgnoreCase("remove")) {
					try {
						if (blockList.contains(blockID)) {
							this.removeBlock(blockID);

							Chat.printClientMessage("Block " + blockID + " has been removed.");
							Wrapper.getMinecraft().renderGlobal.loadRenderers();
						} else {
							Chat.printClientMessage("Block " + blockID + " hasn't been added yet!");
						}
					} catch (Exception x) {
						x.printStackTrace();
					}
				} else {
					Chat.printClientMessage("Unknown subcommand!");
				}
			} else {
				if (args[0].equalsIgnoreCase("list")) {
					Chat.printClientMessage("Xray Block List:");
					for (int id : blockList) {
						Chat.printClientMessage("Block: " + id);
					}
				} else if (args[0].equalsIgnoreCase("clear")) {
					blockList.clear();
					Chat.printClientMessage("Xray list cleared.");
					Wrapper.getMinecraft().renderGlobal.loadRenderers();
				} else if (args[0].equalsIgnoreCase("gui")) {
					GuiPanel guiPanel = Wrapper.getClickableGui().getGuiPanel("Xray Blocks");
					guiPanel.setVisible(!guiPanel.isVisible());
					Chat.printClientMessage("Xray Block Panel is now " + (guiPanel.isVisible() ? "visible in" : "hidden from") + " the clickable gui.");
				} else {
					Chat.printClientMessage("Unknown subcommand!");
				}
			}
		} else {
			Chat.printClientMessage("Too few arguments!");
		}
	}

	public void removeBlock(int blockID) {
		for (int i = 0; i < blockList.size(); i++) {
			int id = blockList.get(i);

			if (id == blockID) {
				blockList.remove(i);
			}
		}
	}

	@Override
	public void onDisable() {
		Wrapper.getWorld().provider.generateLightBrightnessTable();
		Wrapper.getEventManager().unregister(this);

		if (this.editedAmbientOcclusion) {
			Wrapper.getGameSettings().ambientOcclusion = 0;
		}
	}

	@Override
	public void onShutdown() {
		blockList.forEach((blockID) -> this.getModuleSettings().put(String.valueOf(blockID), new ClientSettingInteger(String.valueOf(blockID), blockID)));
	}
}
