package io.github.rreeggkk.jari.common.crafting.metal;

import io.github.rreeggkk.jari.common.item.ItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import org.apfloat.Apfloat;

public class MetalAddition implements IRecipe {
 	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
 		int amountMetal = 0;
 		
 		for (int x = 0; x < 3; x++) {
 	 		for (int y = 0; y < 3; y++) {
 	 			if (crafting.getStackInRowAndColumn(x, y) != null) {
 	 	 			if (crafting.getStackInRowAndColumn(x, y).getItem() == ItemRegistry.metalLump) {
 	 	 				amountMetal++;
 	 	 			} else {
 	 	 				return false;
 	 	 			}
 	 			}
 	 		}
 		}
 		
 		return amountMetal >= 2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
 		ArrayList<ItemStack> metalStacks = new ArrayList<ItemStack>();
 		
 		for (int x = 0; x < 3; x++) {
 	 		for (int y = 0; y < 3; y++) {
 	 			if (crafting.getStackInRowAndColumn(x, y) != null) {
 	 	 			if (crafting.getStackInRowAndColumn(x, y).getItem() == ItemRegistry.metalLump) {
 	 	 				metalStacks.add(crafting.getStackInRowAndColumn(x, y));
 	 	 			} else {
 	 	 				return null;
 	 	 			}
 	 			}
 	 		}
 		}
 		
 		ItemStack stack = new ItemStack(ItemRegistry.metalLump);
 		for (ItemStack metalStack : metalStacks) {
 			HashMap<String, Apfloat> cont = ItemRegistry.metalLump.getContents(metalStack);
 			for (String str : cont.keySet()) {
 				ItemRegistry.metalLump.addMetalToLump(stack, str, cont.get(str));
 			}
 		}
 		
 		return stack;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ItemRegistry.metalLump);
	}
}
