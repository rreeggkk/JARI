package io.github.rreeggkk.jari.common.crafting.hydraulic;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public interface IHydraulicRecipe {
	public int getRequiredEnergy();
	public int getRequiredWater();
	public boolean isInput(ItemStack input);
	public ItemStack getResult(ItemStack input);
}
