package de.paxii.clarinet.util.chat;

import de.paxii.clarinet.Wrapper;

import net.minecraft.event.ClickEvent;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

import lombok.Getter;

public class Chat {
  @Getter
  private static final String prefix = ChatColor.AQUA + "[C] " + ChatColor.WHITE;
  private static final Pattern URL_PATTERN = Pattern.compile("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

  public static void printChatMessage(String chatMessage) {
    IChatComponent textComponent = new ChatComponentText("");
    String[] messageParts = chatMessage.split(" ");
    int pathIndex = 0;

    for (String messagePart : messageParts) {
      IChatComponent append = new ChatComponentText(messagePart);
      ChatStyle chatStyle = new ChatStyle();

      if (Chat.URL_PATTERN.matcher(ChatColor.stripColor(messagePart)).matches()) {
        chatStyle.setUnderlined(true);
        chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ChatColor.stripColor(messagePart)));
      }

      String currentPath = chatMessage.substring(0, chatMessage.indexOf(messagePart, pathIndex));
      String lastColor = ChatColor.getLastColors(currentPath);

      if (lastColor.length() >= 2) {
        char formattingChar = lastColor.charAt(1);
        Chat.formatChatStyle(chatStyle, formattingChar);
      }

      append.setChatStyle(chatStyle);
      textComponent.appendSibling(append);
      textComponent.appendText(" ");
      pathIndex += messagePart.length() - 1;
    }

    Wrapper.getMinecraft().getIngameGUI().getChatGUI()
            .printChatMessage(textComponent);
  }

  public static void printChatComponent(IChatComponent textComponent) {
    Wrapper.getMinecraft().getIngameGUI().getChatGUI()
            .printChatMessage(textComponent);
  }

  public static void printClientMessage(String chatMessage) {
    printChatMessage(prefix + chatMessage);
  }

  public static void sendChatMessage(String chatMessage) {
    Wrapper.getSendQueue().addToSendQueue(new C01PacketChatMessage("_PASS_" + chatMessage));
  }

  private static EnumChatFormatting getTextFormattingByValue(char value) {
    for (EnumChatFormatting textFormatting : EnumChatFormatting.values()) {
      if (value == textFormatting.getColorIndex()) {
        return textFormatting;
      }
    }

    return null;
  }

  private static void formatChatStyle(ChatStyle chatStyle, char formattingChar) {
    switch (formattingChar) {
      case 'k':
        chatStyle.setObfuscated(true);
        break;
      case 'm':
        chatStyle.setStrikethrough(true);
        break;
      case 'l':
        chatStyle.setBold(true);
        break;
      case 'n':
        chatStyle.setUnderlined(true);
        break;
      case 'o':
        chatStyle.setItalic(true);
        break;
      case 'r':
        chatStyle.setObfuscated(false);
        chatStyle.setStrikethrough(false);
        chatStyle.setBold(false);
        chatStyle.setUnderlined(false);
        chatStyle.setItalic(false);
        chatStyle.setColor(EnumChatFormatting.RESET);
        break;
      default:
        EnumChatFormatting textFormatting = Chat.getTextFormattingByValue(formattingChar);
        chatStyle.setColor(textFormatting != null ? textFormatting : EnumChatFormatting.RESET);
    }
  }
}
