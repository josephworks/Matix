package de.paxii.clarinet.gui.menu.changelog;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.util.web.JsonFetcher;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import lombok.Getter;

public class GuiChangelog extends GuiScreen {

  private GuiScreen parentScreen;

  private Changelog changelog;

  private int posY;

  /**
   * An empty List used to draw the dark background
   */
  private GuiOptionsRowList guiOptionsRowList;

  public GuiChangelog() {
    this.parentScreen = Wrapper.getMinecraft().currentScreen;
  }

  @Override
  public void initGui() {
    this.guiOptionsRowList = new GuiOptionsRowList(
            Wrapper.getMinecraft(),
            Wrapper.getScaledResolution().getScaledWidth(),
            Wrapper.getScaledResolution().getScaledHeight(),
            20,
            Wrapper.getScaledResolution().getScaledHeight() - 30,
            20
    );
    this.changelog = JsonFetcher.get(Client.getClientURL() + "changelog.json", Changelog.class);
    this.buttonList.add(new GuiButton(1, 10, this.height - 25, 100, 20, "Back"));
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();

    this.guiOptionsRowList.drawScreen(mouseX, mouseY, partialTicks);

    this.drawCenteredString(
            this.fontRenderer,
            String.format("%s Changelog (running %s)", Client.getClientName(), Client.getClientVersion()),
            this.width / 2,
            6,
            0xffffff
    );

    this.posY = 30;
    if (this.changelog != null) {
      this.changelog.getChangelog().forEach((version, entry) -> {
        this.drawString(version, 0xffffff);
        Arrays.stream(entry.getAdded()).forEach(label -> this.drawString(label, 0x00ff00, "+ ", 10));
        Arrays.stream(entry.getRemoved()).forEach(label -> this.drawString(label, 0xff0000, "- ", 10));
        Arrays.stream(entry.getFixed()).forEach(label -> this.drawString(label, 0xff8800, "* ", 10));
        Arrays.stream(entry.getNotes()).forEach(label -> this.drawString(label, 0x0077ff, "# ", 10));
      });
    } else {
      this.drawString("is null y0", 0xff0000);
    }

    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 1) {
      Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
    }

    super.actionPerformed(button);
  }

  private void drawString(String label, int color) {
    this.drawString(label, color, "", 0);
  }

  private void drawString(String label, int color, String prefix, int posX) {
    if (!this.shouldDraw()) {
      return;
    }

    String prefixOffset = prefix.replaceAll(".", " ");
    this.fontRenderer.drawString(prefix, 10 + posX, posY, color);
    while (label.length() > 0) {
      if (!this.shouldDraw()) {
        return;
      }
      int firstLineFeed = label.contains("\n") ? label.indexOf("\n") : label.length();
      String line = label.substring(0, firstLineFeed);
      this.fontRenderer.drawString(prefixOffset + line, 10 + posX, this.posY, color);
      this.posY += 10;
      label = label.substring(label.length() >= firstLineFeed + 1 ? firstLineFeed + 1 : label.length());
    }
  }

  private boolean shouldDraw() {
    return posY < this.height - 40;
  }

  @Getter
  private class Changelog {
    private LinkedHashMap<String, Entry> changelog;

    public Changelog(LinkedHashMap<String, Entry> changelog) {
      this.changelog = changelog;
    }
  }

  @Getter
  private class Entry {
    private String[] added;
    private String[] removed;
    private String[] fixed;
    private String[] notes;

    public Entry(String[] added, String[] removed, String[] fixed, String[] notes) {
      this.added = added;
      this.removed = removed;
      this.fixed = fixed;
      this.notes = notes;
    }
  }
}
