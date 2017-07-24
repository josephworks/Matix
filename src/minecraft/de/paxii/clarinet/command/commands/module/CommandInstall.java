package de.paxii.clarinet.command.commands.module;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.gui.menu.store.module.GuiModuleStore;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * Created by Lars on 07.02.2016.
 */
public class CommandInstall extends AClientCommand {
  @Override
  public String getCommand() {
    return "install";
  }

  @Override
  public String getDescription() {
    return "Opens the plugins menu.";
  }

  @Override
  public void runCommand(String[] args) {
    new Thread(() -> {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        Wrapper.getMinecraft().displayGuiScreen(new GuiModuleStore(null));
        //TODO: Client Eh what?
        // This prevents the Cursor from being invisible
        // Find some other way, this is ugly.
        Mouse.setCursorPosition(-999, -999);
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
      }
    }).start();
  }

  @Override
  public String getUsage() {
    return "install";
  }

  @Override
  public CommandCategory getCategory() {
    return CommandCategory.MODULE;
  }
}
