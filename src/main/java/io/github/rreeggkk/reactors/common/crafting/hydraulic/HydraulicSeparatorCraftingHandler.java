package io.github.rreeggkk.reactors.common.crafting.hydraulic;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class HydraulicSeparatorCraftingHandler {

	public static HydraulicSeparatorCraftingHandler instance = new HydraulicSeparatorCraftingHandler();
	private ArrayList<IHydraulicRecipe> recipes = new ArrayList<IHydraulicRecipe>();
	
	static {
		instance.addRecipe(new ItemStack(Blocks.log), new ItemStack(Blocks.planks), 50000);
		instance.addRecipe("oreGold", "dustGold", 3, 5000);
		instance.addRecipe("oreIron", "dustIron", 3, 5000);
		instance.addRecipe("oreCopper", "dustCopper", 3, 5000);
		instance.addRecipe("oreTin", "dustTin", 3, 5000);
		instance.addRecipe("oreCoal", "dustCoal", 3, 5000);
		instance.addRecipe(new OreHydraulicRecipe("oreLapis", 1, new ItemStack(Items.dye, 18, 4), 5000));
		instance.addRecipe(new OreHydraulicRecipe("oreDiamond", 1, new ItemStack(Items.diamond, 3), 10000));
		instance.addRecipe("oreRedstone", "dustRedstone", 18, 5000);
		instance.addRecipe(new OreHydraulicRecipe("oreEmerald", 1, new ItemStack(Items.emerald, 3), 10000));
		instance.addRecipe(new OreHydraulicRecipe("oreNetherQuartz", 1, new ItemStack(Items.quartz, 5), 4000));
		instance.addRecipe("oreSilver", "dustSilver", 3, 5000);
		instance.addRecipe("oreLead", "dustLead", 3, 5000);
		instance.addRecipe("oreNickel", "dustNickel", 3, 5000);
		instance.addRecipe("orePlatinum", "dustPlatinum", 3, 5000);
	}

	public void addRecipe(ItemStack input, ItemStack output, int energy){
		recipes.add(new HydraulicRecipe(input, output, energy));
	}
	public void addRecipe(String input, String output, int energy){
		recipes.add(new OreHydraulicRecipe(input, output, energy));
	}
	public void addRecipe(String input, String output, int outputNum, int energy){
		recipes.add(new OreHydraulicRecipe(input, output, outputNum, energy));
	}
	public void addRecipe(String input, int inputNum, String output, int outputNum, int energy){
		recipes.add(new OreHydraulicRecipe(input, inputNum, output, outputNum, energy));
	}
	public void addRecipe(IHydraulicRecipe r){
		recipes.add(r);
	}
	public IHydraulicRecipe getRecipeForInput(ItemStack input) {
		for (IHydraulicRecipe r : recipes) {
			if (r.isInput(input)) {
				return r;
			}
		}
		return null;
	}
}