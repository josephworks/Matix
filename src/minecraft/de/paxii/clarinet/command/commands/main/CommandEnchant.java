package de.paxii.clarinet.command.commands.main;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.command.AClientCommand;
import de.paxii.clarinet.command.CommandCategory;
import de.paxii.clarinet.util.chat.Chat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Created by Lars on 28.02.2016.
 */
public class CommandEnchant extends AClientCommand {
	@Override
	public String getCommand() {
		return "enchant";
	}

	@Override
	public String getDescription() {
		return "Sets the Enchantments for the current Item to 127. Requires Creative Mode.";
	}

	@Override
	public void runCommand(String[] args) {
		if (Wrapper.getPlayer().capabilities.isCreativeMode) {
			ItemStack currentItem = Wrapper.getPlayer().getHeldItem(EnumHand.MAIN_HAND);

			if (currentItem != null) {
				for (Enchantment enchantment : Enchantment.REGISTRY) {
					if (enchantment == Enchantment.getEnchantmentByID(33)) {
						continue;
					}

					try {
						currentItem.addEnchantment(enchantment, 127);
					} catch (Exception ignored) {
					}
				}

				Chat.printClientMessage("Your Item has been enchanted!");
			} else {
				Chat.printClientMessage("You need to have an Item in Hand!");
			}
		} else {
			Chat.printClientMessage("This Command requires you to be in Creative Mode.");
		}
	}

	@Override
	public String getUsage() {
		return this.getCommand();
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MAIN;
	}
}
