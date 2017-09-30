package com.dom.robot.items;

import java.util.Set;

import com.dom.robot.RobotMod;
import com.dom.robot.reference.Reference;
import com.dom.robot.utils.EnchantmentUtils;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemRecombobulator extends ItemTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {});

	public ItemRecombobulator(Item.ToolMaterial material) {
		super(0, 0, material, ItemRecombobulator.EFFECTIVE_ON);
		setUnlocalizedName("robot_printer");
		setCreativeTab(RobotMod.roboTab);
		setRegistryName(Reference.MOD_ID, getUnlocalizedName());
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	// Make a unique name for each contents type (lime, orange, etc) so we can
	// name them individually
	// The fullness information is added separately in getItemStackDisplayName()
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "_" + stack.getMetadata();
	}

	/**
	 * Allow or forbid the specific book/item combination as an anvil enchant
	 *
	 * @param stack
	 *            The item
	 * @param book
	 *            The book
	 * @return if the enchantment is allowed
	 */
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if (EnchantmentUtils.hasEnchant(Enchantments.EFFICIENCY, ItemEnchantedBook.getEnchantments(book))) {
			return true;
		}
		return false;
	}

	/**
	 * Called when the player Left Clicks (attacks) an entity. Processed before
	 * damage is done, if return value is true further processing is canceled and
	 * the entity is not attacked.
	 *
	 * @param stack
	 *            The Item being used
	 * @param player
	 *            The player that is attacking
	 * @param entity
	 *            The entity being attacked
	 * @return True to cancel the rest of the interaction.
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return true;
	}
}
