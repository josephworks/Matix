package de.paxii.clarinet.util.gui;

import de.paxii.clarinet.Wrapper;

import org.lwjgl.input.Mouse;

public class MouseService {

  public static int getX() {
    return Mouse.getX() * Wrapper.getScaledResolution().getScaledWidth() / Wrapper.getMinecraft().displayWidth;
  }

  public static int getY() {
    int scaledHeight = Wrapper.getScaledResolution().getScaledHeight();

    return scaledHeight - Mouse.getY() * scaledHeight / Wrapper.getMinecraft().displayHeight - 1;
  }

}
