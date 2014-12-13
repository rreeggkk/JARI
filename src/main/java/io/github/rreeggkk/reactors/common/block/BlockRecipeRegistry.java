package io.github.rreeggkk.reactors.common.block;

/*
 * General place to do all your block related recipe things'n'stuff.
 */

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRecipeRegistry {

	private static void registerShapedRecipes() {
	}

	private static void registerShaplessRecipes() {

	}

	public static void registerBlockRecipes() {
		registerShapedRecipes();
		registerShaplessRecipes();
	}
}
