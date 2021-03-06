package de.paxii.clarinet.util.chat.font;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.StartGameEvent;

import java.io.File;
import java.io.InputStream;

import lombok.Getter;

/**
 * Created by Lars on 26.06.2016.
 */
public class FontManager {
  @Getter
  private static TTF defaultFont;
  @Getter
  private static TTF smallFont;
  @Getter
  private static TTF bigUbuntuFont;
  @Getter
  private static TTF ubuntuFont;
  @Getter
  private static TTF smallUbuntuFont;

  public FontManager() {
    Wrapper.getEventManager().register(this);
  }

  @EventHandler
  private void loadFonts(StartGameEvent event) {
    FontManager.defaultFont = new TTF("Verdana", 16);
    FontManager.smallFont = new TTF("Verdana", 8);
    FontManager.bigUbuntuFont = new TTF(this.getFontStream("Ubuntu/Ubuntu-Bold.ttf"), 20);
    FontManager.ubuntuFont = new TTF(this.getFontStream("Ubuntu/Ubuntu-Regular.ttf"), 18);
    FontManager.smallUbuntuFont = new TTF(this.getFontStream("Ubuntu/Ubuntu-Bold.ttf"), 18);
  }

  private InputStream getFontStream(String fontName) {
    return FontManager.class.getResourceAsStream(String.format("/assets/matix/fonts/%s", fontName));
  }
}
