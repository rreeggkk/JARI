package io.github.rreeggkk.jari.common.item;

/*
 * General place to do all your item related recipe things'n'stuff.
 */

import io.github.rreeggkk.jari.common.block.BlockRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRecipeRegistry {

	private static void registerShapedRecipes() {
	}

	private static void registerShaplessRecipes() {
	}

	public static void registerItemRecipes() {
		registerShapedRecipes();
		registerShaplessRecipes();
	}
}
