package io.github.rreeggkk.jari.common.crafting.hydraulic;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class HydraulicRecipe implements IHydraulicRecipe {
	
	private ItemStack input, output;
	private int energy, water;
	
	public HydraulicRecipe(ItemStack input, ItemStack output, int energy, int water) {
		this.input = input;
		this.output = output;
		this.energy = energy;
		this.water = water;
	}

	@Override
	public int getRequiredEnergy() {
		return energy;
	}

	@Override
	public boolean isInput(ItemStack input) {
		return (input.isItemEqual(this.input)) && (this.input.stackSize <= input.stackSize);
	}

	@Override
	public ItemStack getResult(ItemStack input) {
		return output;
	}

	@Override
	public int getRequiredWater() {
		return water;
	}
}
