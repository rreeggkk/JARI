package io.github.rreeggkk.jari.common.block;

import net.minecraft.item.ItemStack;
import cofh.thermalexpansion.block.TEBlocks;
import cofh.thermalexpansion.block.cell.BlockCell;
import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.block.simple.BlockGlass;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.item.TEItems;
import cofh.thermalfoundation.block.TFBlocks;
import cofh.thermalfoundation.item.TFItems;
import cpw.mods.fml.common.registry.GameRegistry;

/*
 * General place to do all your block related recipe things'n'stuff.
 */


public class BlockRecipeRegistry {

	private static void registerShapedRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(BlockRegistry.blockHydraulicSeparator),
				"iti", //	Invar		Tank		Invar
				"hph", //	HGlass		Pulverizer	HGlass
				"ele", //	Electrum	BasicCell	Electrum
				'i', TFItems.ingotInvar,
				't', BlockTank.tankBasic,
				'h', BlockGlass.glassHardened,
				'p', BlockMachine.pulverizer,
				'e', TFItems.ingotElectrum,
				'l', BlockCell.cellBasic);
	}

	private static void registerShaplessRecipes() {

	}

	public static void registerBlockRecipes() {
		registerShapedRecipes();
		registerShaplessRecipes();
	}
}
