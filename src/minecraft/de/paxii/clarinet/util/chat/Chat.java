package de.paxii.clarinet.util.chat;

import de.paxii.clarinet.Wrapper;
import lombok.Getter;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import java.util.regex.Pattern;

public class Chat {
	@Getter
	private static final String prefix = ChatColor.AQUA + "[C] " + ChatColor.WHITE;
	private static final Pattern URL_PATTERN = Pattern.compile("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

	public static void printChatMessage(String chatMessage) {
		ITextComponent textComponent = new TextComponentString("");
		String[] messageParts = chatMessage.split(" ");
		int pathIndex = 0;

		for (String messagePart : messageParts) {
			ITextComponent append = new TextComponentString(messagePart);
			Style chatStyle = new Style();

			if (Chat.URL_PATTERN.matcher(ChatColor.stripColor(messagePart)).matches()) {
				chatStyle.setUnderlined(true);
				chatStyle.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ChatColor.stripColor(messagePart)));
			}

			String currentPath = chatMessage.substring(0, chatMessage.indexOf(messagePart, pathIndex));
			String lastColor = ChatColor.getLastColors(currentPath);

			if (lastColor.length() >= 2) {
				char formattingChar = lastColor.charAt(1);
				Chat.formatChatStyle(chatStyle, formattingChar);
			}

			append.setStyle(chatStyle);
			textComponent.appendSibling(append);
			textComponent.appendText(" ");
			pathIndex += messagePart.length() - 1;
		}

		Wrapper.getMinecraft().getIngameGUI().getChatGUI()
		       .printChatMessage(textComponent);
	}

	public static void printChatComponent(ITextComponent textComponent) {
		Wrapper.getMinecraft().getIngameGUI().getChatGUI()
		       .printChatMessage(textComponent);
	}

	public static void printClientMessage(String chatMessage) {
		printChatMessage(prefix + chatMessage);
	}

	public static void sendChatMessage(String chatMessage) {
		Wrapper.getSendQueue().addToSendQueue(new CPacketChatMessage("_PASS_" + chatMessage));
	}

	private static TextFormatting getTextFormattingByValue(char value) {
		for (TextFormatting textFormatting : TextFormatting.values()) {
			if (value == textFormatting.getFormattingCode()) {
				return textFormatting;
			}
		}

		return null;
	}

	private static void formatChatStyle(Style chatStyle, char formattingChar) {
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
				chatStyle.setColor(TextFormatting.RESET);
				break;
			default:
				TextFormatting textFormatting = Chat.getTextFormattingByValue(formattingChar);
				chatStyle.setColor(textFormatting != null ? textFormatting : TextFormatting.RESET);
		}
	}
}
