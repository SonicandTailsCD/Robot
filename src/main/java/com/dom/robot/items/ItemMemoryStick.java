package com.dom.robot.items;

import com.dom.robot.RobotMod;
import com.dom.robot.reference.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMemoryStick extends Item {

	public ItemMemoryStick() {
		super();
		setHasSubtypes(true);
		setMaxStackSize(1);
		setUnlocalizedName("robot_memory");
		setRegistryName(Reference.MOD_ID, getUnlocalizedName());
		setCreativeTab(RobotMod.roboTab);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	// add a subitem for each item we want to appear in the creative tab
	// in this case - a full bottle of each colour
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
		for (int i = 1; i < 8; i++) {
			items.add(new ItemStack(this, 1, i));
		}
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
		return false;
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	@Override
	public boolean isEnchantable(ItemStack stack) {
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
