package io.github.rreeggkk.reactors.common.crafting.hydraulic;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreHydraulicRecipe implements IHydraulicRecipe {
	
	private int input;
	private int inputNum;
	private ItemStack output;
	private int energy;
	
	public OreHydraulicRecipe(String input, String output, int energy) {
		this.input = OreDictionary.getOreID(input);
		ArrayList<ItemStack> ores = OreDictionary.getOres(output);
		
		if (!ores.isEmpty()) {
			this.output = ores.get(0);
		} else {
			this.output = null;
		}
		this.energy = energy;
		inputNum = 1;
	}
	
	public OreHydraulicRecipe(String input, String output, int outputNum, int energy) {
		this.input = OreDictionary.getOreID(input);
		
		ArrayList<ItemStack> ores = OreDictionary.getOres(output);
		
		if (!ores.isEmpty()) {
			this.output = ores.get(0);
		} else {
			this.output = null;
		}
		this.output.stackSize = outputNum;
		this.energy = energy;
		inputNum = 1;
	}

	public OreHydraulicRecipe(String input, int inputNum, String output, int outputNum, int energy) {
		this.input = OreDictionary.getOreID(input);
		this.inputNum = inputNum;
		ArrayList<ItemStack> ores = OreDictionary.getOres(output);
		
		if (!ores.isEmpty()) {
			this.output = ores.get(0);
		} else {
			this.output = null;
		}
		this.output.stackSize = outputNum;
		this.energy = energy;
	}

	public OreHydraulicRecipe(String input, int inputNum, ItemStack output, int energy) {
		this.input = OreDictionary.getOreID(input);
		this.inputNum = inputNum;
		this.output = output;
		this.energy = energy;
	}

	public OreHydraulicRecipe(ItemStack input, ItemStack output, int energy) {
		this.input = OreDictionary.getOreIDs(input)[0];
		inputNum = input.stackSize;
		this.output = output;
		this.energy = energy;
	}

	@Override
	public int getRequiredEnergy() {
		return energy;
	}

	@Override
	public boolean isInput(ItemStack input) {
		for (int i : OreDictionary.getOreIDs(input)) {
			if ((this.input == i) && (input.stackSize >= inputNum)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public ItemStack getResult(ItemStack input) {
		return output;
	}
}
