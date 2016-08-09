package de.paxii.clarinet.module.player;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.player.PlayerAttackEntityEvent;
import de.paxii.clarinet.event.events.player.PlayerClickBlockEvent;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.util.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;

public class ModuleAutoTool extends Module {
	public ModuleAutoTool() {
		super("AutoTool", ModuleCategory.PLAYER, -1);

		this.setRegistered(true);
		this.setDescription("Automatically uses the best tool currently in the hotbar.");
	}

	@EventHandler
	public void onClickBlock(PlayerClickBlockEvent event) {
		BlockPos blockPos = event.getBlockPos();
		autoTool(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@EventHandler
	public void onAttackEntity(PlayerAttackEntityEvent event) {
		bestSword(event.getTarget());
	}

	private void autoTool(int i, int j, int k) {
		Block b = Wrapper.getWorld().getBlock(i, j, k);

		if (b.getMaterial() != Material.AIR) {
			float s = 0.1F;
			int currentItem = Wrapper.getPlayer().inventory.currentItem;

			for (int inventoryIndex = 36; inventoryIndex < 45; ++inventoryIndex) {
				ItemStack is = Wrapper.getPlayer().openContainer.getSlot(
						inventoryIndex).getStack();

				if (is != null) {
					if (Wrapper.getPlayer().capabilities.isCreativeMode
							&& is.getItem() instanceof ItemSword) {
						continue;
					}

					float strength = PlayerUtils.getPlayerStrVsBlock(b, is);

					if (strength > s) {
						s = strength;
						currentItem = inventoryIndex - 36;
					}
				}
			}

			Wrapper.getPlayer().inventory.currentItem = currentItem;
		}
	}

	private void bestSword(Entity targetEntity) {
		int bestSlot = 0;
		float damage = 1.0F;

		for (int inventoryIndex = 36; inventoryIndex < 45; inventoryIndex++) {
			if ((Wrapper.getPlayer().inventoryContainer.inventorySlots
					.toArray())[inventoryIndex] != null && targetEntity != null) {
				ItemStack curSlot = Wrapper.getPlayer().inventoryContainer
						.getSlot(inventoryIndex).getStack();

				if (curSlot != null) {

					float itemDamage = 1.0F + (curSlot.hasEffect() ? getEnchantDamageVsEntity(
							curSlot, targetEntity) : getSwordQuality(curSlot));

					if (itemDamage > damage) {
						bestSlot = inventoryIndex - 36;
						damage = itemDamage;
					}
				}
			}
		}

		if (damage > 1.0F) {

			Wrapper.getPlayer().inventory.currentItem = bestSlot;
		}
	}

	private int getSwordQuality(ItemStack itemStack) {
		Item item = itemStack.getItem();

		if (item == Item.getItemById(276)) {
			return 5;
		} else if (item == Item.getItemById(283)) {
			return 4;
		} else if (item == Item.getItemById(267)) {
			return 3;
		} else if (item == Item.getItemById(272)) {
			return 2;
		} else if (item == Item.getItemById(268)) {
			return 1;
		} else {
			return 0;
		}
	}

	private int getEnchantDamageVsEntity(ItemStack i, Entity e) {
		if (e instanceof EntityZombie
				|| e instanceof EntitySkeleton) {
			return EnchantmentHelper.getEnchantmentLevel(
					Enchantment.getEnchantmentByID(16), i)
					+ EnchantmentHelper.getEnchantmentLevel(
					Enchantment.getEnchantmentByID(17), i);
		} else if (e instanceof EntitySpider) {
			return EnchantmentHelper.getEnchantmentLevel(
					Enchantment.getEnchantmentByID(16), i)
					+ EnchantmentHelper.getEnchantmentLevel(
					Enchantment.getEnchantmentByID(18), i);
		} else {
			return EnchantmentHelper.getEnchantmentLevel(
					Enchantment.getEnchantmentByID(16), i);
		}
	}
}
