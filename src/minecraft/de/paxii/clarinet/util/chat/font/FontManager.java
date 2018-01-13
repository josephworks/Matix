package de.paxii.clarinet.util.chat.font;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.game.StartGameEvent;

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
    FontManager.ubuntuFont = new TTF("Ubuntu Bold", 20);
    FontManager.smallUbuntuFont = new TTF("Ubuntu", 18);
  }
}
