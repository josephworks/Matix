package de.paxii.clarinet.gui.ingame.panel;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.gui.ingame.panel.element.PanelElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class GuiPanel {
	@Getter
	@Setter
	private String panelName;
	@Getter
	@Setter
	private int panelX, panelY, panelWidth, panelHeight, titleHeight = 12, scrollY;
	private int dragX, dragY;
	@Getter
	@Setter
	private boolean dragging, opened, pinned, visible = true, scrollable;
	@Getter
	ArrayList<PanelElement> panelElements;

	public GuiPanel(String panelName, int panelX, int panelY) {
		this(panelName, panelX, panelY, 100, 200);
	}

	public GuiPanel(String panelName, int panelX, int panelY, int panelWidth, int panelHeight) {
		this(panelName, panelX, panelY, panelWidth, panelHeight, true);
	}

	public GuiPanel(String panelName, int panelX, int panelY, int panelWidth, int panelHeight, boolean isOpened) {
		this(panelName, panelX, panelY, panelWidth, panelHeight, isOpened, true);
	}

	public GuiPanel(String panelName, int panelX, int panelY, int panelWidth, int panelHeight, boolean isOpened, boolean isVisible) {
		this.panelName = panelName;
		this.panelX = panelX;
		this.panelY = panelY;
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.panelElements = new ArrayList<>();

		this.setOpened(isOpened);
		this.setVisible(isVisible);

		this.addElements();
	}

	public void addElements() {
	}

	public int calculateHeight() {
		int height = 20;

		for (PanelElement panelElement : this.getPanelElements()) {
			height += panelElement.getElementHeight() + panelElement.getElementYOffset();
		}

		return height;
	}

	public void drawPanel(int mouseX, int mouseY) {
		this.setPanelHeight(this.calculateHeight());

		if (this.isDragging()) {
			this.setPanelX(mouseX + dragX);
			this.setPanelY(mouseY + dragY);
		}

		Wrapper.getClickableGui().getCurrentTheme().drawPanel(this, mouseX, mouseY);
	}

	public void mouseClicked(int mouseX, int mouseY, int buttonClicked) {
		if (this.isMouseOverCollapseButton(mouseX, mouseY) && buttonClicked == 0) {
			this.setOpened(!this.isOpened());

			return;
		}

		if (this.isMouseOverTitle(mouseX, mouseY) && buttonClicked == 0) {
			this.setDragging(true);
			this.dragX = this.getPanelX() - mouseX;
			this.dragY = this.getPanelY() - mouseY;
		}

		if (this.isOpened() && this.isMouseOverPanel(mouseX, mouseY)) {
			for (PanelElement panelElement : this.getPanelElements()) {
				panelElement.mouseClicked(mouseX, mouseY, buttonClicked);
			}
		}
	}

	public boolean isMouseOverPanel(int mouseX, int mouseY) {
		boolean rightX = mouseX >= this.getPanelX()
				&& mouseX <= this.getPanelX() + this.getPanelWidth();
		boolean rightY = mouseY >= this.getPanelY()
				&& mouseY <= this.getPanelY()
				+ (this.isOpened() ? this.getPanelHeight() : this
				.getTitleHeight());

		return rightX && rightY;
	}

	public boolean isMouseOverTitle(int mouseX, int mouseY) {
		boolean rightX = mouseX >= this.getPanelX()
				&& mouseX <= this.getPanelX() + this.getPanelWidth();
		boolean rightY = mouseY >= this.getPanelY()
				&& mouseY <= this.getPanelY() + this.getTitleHeight();

		return rightX && rightY;
	}

	public boolean isMouseOverCollapseButton(int mouseX, int mouseY) {
		return mouseX >= this.getPanelX() + this.getPanelWidth() - 11 &&
				mouseX <= this.getPanelX() + this.getPanelWidth() - 2 &&
				mouseY >= this.getPanelY() + 2 &&
				mouseY <= this.getPanelY() + this.getTitleHeight() - 2;
	}

	public boolean isMouseOverAll(int mouseX, int mouseY) {
		if (isMouseOverPanel(mouseX, mouseY)) {
			for (GuiPanel guiPanel : Wrapper.getClickableGui().getGuiPanels()) {
				if (!guiPanel.isVisible())
					continue;

				if (guiPanel.isMouseOverPanel(mouseX, mouseY)
						&& Wrapper.getClickableGui().getGuiPanels()
						.indexOf(guiPanel) > Wrapper.getClickableGui()
						.getGuiPanels().indexOf(this)) {
					return false;
				}
			}

			return true;
		}

		return false;
	}
}
