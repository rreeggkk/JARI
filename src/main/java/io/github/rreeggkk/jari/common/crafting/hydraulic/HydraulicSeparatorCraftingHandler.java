package io.github.rreeggkk.jari.common.crafting.hydraulic;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HydraulicSeparatorCraftingHandler {

	public static HydraulicSeparatorCraftingHandler instance = new HydraulicSeparatorCraftingHandler();
	private ArrayList<IHydraulicRecipe> recipes = new ArrayList<IHydraulicRecipe>();

	static {
		instance.addRecipe("oreGold", "dustGold", 3, 5000, 1400);
		instance.addRecipe("oreIron", "dustIron", 3, 5000, 1000);
		instance.addRecipe("oreCopper", "dustCopper", 3, 5000, 800);
		instance.addRecipe("oreTin", "dustTin", 3, 5000, 800);
		instance.addRecipe("oreCoal", "dustCoal", 3, 5000, 600);
		instance.addRecipe(new OreHydraulicRecipe("oreLapis", 1, new ItemStack(
				Items.dye, 18, 4), 5000, 1000));
		instance.addRecipe(new OreHydraulicRecipe("oreDiamond", 1,
				new ItemStack(Items.diamond, 3), 10000, 4000));
		instance.addRecipe("oreRedstone", "dustRedstone", 18, 5000, 1000);
		instance.addRecipe(new OreHydraulicRecipe("oreEmerald", 1,
				new ItemStack(Items.emerald, 3), 10000, 4000));
		instance.addRecipe(new OreHydraulicRecipe("oreNetherQuartz", 1,
				new ItemStack(Items.quartz, 5), 4000, 800));
		instance.addRecipe("oreSilver", "dustSilver", 3, 5000, 1200);
		instance.addRecipe("oreLead", "dustLead", 3, 5000, 1000);
		instance.addRecipe("oreNickel", "dustNickel", 3, 5000, 1000);
		instance.addRecipe("orePlatinum", "dustPlatinum", 3, 5000, 1000);

		instance.addRecipe(new RadioactiveStoneHydraulicRecipe());
	}

	/**
	 * Adds a non-OreDictionary recipe
	 *
	 * @param input
	 *            ItemStack (Will use Item type and quantity)
	 * @param output
	 *            ItemStack (Will use Item type and quantity)
	 * @param energy
	 *            required amount of energy
	 * @param water
	 *            required amount of water
	 */
	public void addRecipe(ItemStack input, ItemStack output, int energy,
			int water) {
		recipes.add(new HydraulicRecipe(input, output, energy, water));
	}

	/**
	 * Adds an OreDictionary recipe
	 *
	 * @param input
	 *            String OreDictionary entry
	 * @param output
	 *            String OreDictionary entry
	 * @param energy
	 *            required amount of energy
	 * @param water
	 *            required amount of water
	 */
	public void addRecipe(String input, String output, int energy, int water) {
		recipes.add(new OreHydraulicRecipe(input, output, energy, water));
	}

	/**
	 * Adds an OreDictionary recipe
	 *
	 * @param input
	 *            String OreDictionary entry
	 * @param output
	 *            String OreDictionary entry
	 * @param outputNum
	 *            number of items for output
	 * @param energy
	 *            required amount of energy
	 * @param water
	 *            required amount of water
	 */
	public void addRecipe(String input, String output, int outputNum,
			int energy, int water) {
		recipes.add(new OreHydraulicRecipe(input, output, outputNum, energy,
				water));
	}

	/**
	 * Adds an OreDictionary recipe
	 *
	 * @param input
	 *            String OreDictionary entry
	 * @param inputNum
	 *            number of items for input
	 * @param output
	 *            String OreDictionary entry
	 * @param outputNum
	 *            number of items for output
	 * @param energy
	 *            required amount of energy
	 * @param water
	 *            required amount of water
	 */
	public void addRecipe(String input, int inputNum, String output,
			int outputNum, int energy, int water) {
		recipes.add(new OreHydraulicRecipe(input, inputNum, output, outputNum,
				energy, water));
	}

	/**
	 * Add a custom recipe
	 *
	 * @param r
	 *            the recipe to add
	 */
	public void addRecipe(IHydraulicRecipe r) {
		recipes.add(r);
	}

	/**
	 * Get the recipe that accepts the ItemStack as input
	 *
	 * @param input
	 *            ItemStack input for recipe
	 * @return a recipe that accepts the ItemStack or null
	 */
	public IHydraulicRecipe getRecipeForInput(ItemStack input) {
		for (IHydraulicRecipe r : recipes) {
			if (r.isInput(input)) {
				return r;
			}
		}
		return null;
	}
}