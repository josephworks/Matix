package de.paxii.clarinet.gui.ingame.panel;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.ClientClickableGui;
import de.paxii.clarinet.gui.ingame.panel.element.elements.*;
import de.paxii.clarinet.gui.ingame.panel.theme.themes.DefaultClientTheme;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.objects.IntObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Lars on 27.05.2016.
 */
public class GuiPanelManager {

	public void loadPanels(ClientClickableGui clickableGui) {
		int xIndex = 5;
		int yIndex = 5;

		ArrayList<GuiPanel> savedPanels = new ArrayList<>();
		if (!clickableGui.getGuiPanels().isEmpty()) {
			savedPanels.addAll(clickableGui.getGuiPanels());
			clickableGui.getGuiPanels().clear();
		}

		for (final ModuleCategory moduleCategory : ModuleCategory.values()) {
			final TreeMap<String, Module> sortedKeys = new TreeMap<>(Wrapper.getModuleManager().getModulesByCategory(moduleCategory));

			clickableGui.getGuiPanels().add(new GuiPanel(moduleCategory.toString(), xIndex, yIndex) {
				@Override
				public void addElements() {
					sortedKeys.forEach((moduleName, module) -> {
						if (module.isDisplayedInGui()) {
							this.getPanelElements().add(new PanelButton(module));
						}
					});
				}
			});

			xIndex += 110;

			ScaledResolution scaledResolution = Wrapper.getScaledResolution();
			if (xIndex >= scaledResolution.getScaledWidth()) {
				xIndex = 5;
				yIndex += clickableGui.getGuiPanels().get(0).getPanelHeight() + 20;
			}
		}

		clickableGui.getGuiPanels().add(new GuiPanel("Settings", xIndex, yIndex, 120, 200) {
			@Override
			public void addElements() {
				Collections.sort(ValueBase.getValueList());

				ValueBase.getValueList().forEach((valueBase) ->
						this.getPanelElements().add(new PanelSlider(valueBase, valueBase.isShouldRound())));
			}
		});

		xIndex += clickableGui.getGuiPanel("Settings").getPanelWidth() + 10;

		if (clickableGui.getCurrentTheme().getName().equals("Default")) {
			clickableGui.getGuiPanels().add(new GuiPanel("Gui Color", xIndex, yIndex, 100, 200) {
				@Override
				public void addElements() {
					DefaultClientTheme defaultTheme = (DefaultClientTheme) clickableGui.getCurrentTheme();

					defaultTheme.getColorObjects().forEach((colorObject) ->
							this.getPanelElements().add(new PanelColorButton(colorObject)));
				}
			});
		}

		xIndex = 5;
		yIndex = clickableGui.getGuiPanel("Player").getPanelY() + clickableGui.getGuiPanel("Player").getPanelHeight() + 20;

		clickableGui.getGuiPanels().add(new GuiPanel("Xray Blocks", xIndex, yIndex, 310, 200, false, false) {
			@Override
			public void addElements() {
				IntObject blockIndex = new IntObject(0);
				List<PanelBlockButton> blockButtons = new ArrayList<>();

				Iterator<Block> it = Block.REGISTRY.iterator();
				while (it.hasNext()) {
					Block block = it.next();
					if (block.getMaterial() != Material.AIR) {
						AtomicBoolean atomicBoolean = new AtomicBoolean(false);
						this.getPanelElements().forEach((panelElement -> {
							if (panelElement instanceof PanelBlockRow) {
								PanelBlockRow currentRow = (PanelBlockRow) panelElement;

								currentRow.getBlockButtons().forEach((blockButton -> {
									if (block.getUnlocalizedName().equals(blockButton.getIBlockState().getBlock().getUnlocalizedName())) {
										atomicBoolean.set(true);
									}
								}));
							}
						}));

						if (!atomicBoolean.get()) {
							blockButtons.add(new PanelBlockButton(block.getDefaultState()));
							blockIndex.add(1);

							if (blockIndex.getInteger() == 15 || !it.hasNext()) {
								this.getPanelElements().add(new PanelBlockRow(blockButtons.toArray(new PanelBlockButton[blockButtons.size()])));
								blockButtons.clear();
								blockIndex.setInteger(0);
							}
						}
					}
				}
			}
		});

		savedPanels.forEach((savedPanel) ->
				clickableGui.getGuiPanels().forEach(guiPanel -> {
					if (savedPanel.getPanelName().equals(guiPanel.getPanelName())) {
						guiPanel.setPanelX(savedPanel.getPanelX());
						guiPanel.setPanelY(savedPanel.getPanelY());
						guiPanel.setOpened(savedPanel.isOpened());
						guiPanel.setPinned(savedPanel.isPinned());
					}
				})
		);
	}
}
