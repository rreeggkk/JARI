package io.github.rreeggkk.reactors.common.crafting.hydraulic;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class HydraulicRecipe implements IHydraulicRecipe {
	
	private ItemStack input, output;
	private int energy;
	
	public HydraulicRecipe(ItemStack input, ItemStack output, int energy) {
		this.input = input;
		this.output = output;
		this.energy = energy;
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
}
