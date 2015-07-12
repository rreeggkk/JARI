package io.github.rreeggkk.jari.common.item;

import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import io.github.rreeggkk.jari.common.crafting.metal.MetalAddition;
import io.github.rreeggkk.jari.common.crafting.metal.MetalHalving;
import cpw.mods.fml.common.registry.GameRegistry;

/*
 * General place to do all your item related recipe things'n'stuff.
 */

public class ItemRecipeRegistry {

	private static void registerShapedRecipes() {
	}

	private static void registerShaplessRecipes() {
		RecipeSorter.register("jari:metaladdition", MetalAddition.class, Category.SHAPED, "after:minecraft:shaped");
		GameRegistry.addRecipe(new MetalAddition());
		RecipeSorter.register("jari:metalhalving", MetalHalving.class, Category.SHAPED, "after:minecraft:shaped");
		GameRegistry.addRecipe(new MetalHalving());
	}

	public static void registerItemRecipes() {
		registerShapedRecipes();
		registerShaplessRecipes();
	}
}
