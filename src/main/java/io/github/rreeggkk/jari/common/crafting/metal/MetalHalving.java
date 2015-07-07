package io.github.rreeggkk.jari.common.crafting.metal;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.elements.ElementRegistry;
import io.github.rreeggkk.jari.common.elements.provider.IElementProvider;
import io.github.rreeggkk.jari.common.elements.provider.IngotProvider;
import io.github.rreeggkk.jari.common.item.ItemRegistry;

import java.util.HashMap;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class MetalHalving implements IRecipe {

	public static MetalHalving instance;

	public MetalHalving() {
		instance = this;
	}

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

		return amountMetal == 1;
	}

	public boolean isConvertToIngot(ItemStack stack) {
		JARI.logger.error("checking if it is convertable");
		HashMap<String, Double> map = ItemRegistry.metalLump.getContents(stack);
		if (map.size() == 1) {
			JARI.logger.error("map size 1");
			String element = (String) map.keySet().toArray()[0];
			IElementProvider provider = ElementRegistry.getProviderForElement(element);
			if (provider instanceof IngotProvider) {
				JARI.logger.error("provider ingot");
				IngotProvider ingot = (IngotProvider) provider;
				if (map.get(element) >= ingot.getAmt()) {
					JARI.logger.error("has enough mass");
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		ItemStack stack = null;

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (crafting.getStackInRowAndColumn(x, y) != null) {
					if (crafting.getStackInRowAndColumn(x, y).getItem() == ItemRegistry.metalLump) {
						stack = crafting.getStackInRowAndColumn(x, y);
					} else {
						return null;
					}
				}
			}
		}

		if (isConvertToIngot(stack)) {
			HashMap<String, Double> map = ItemRegistry.metalLump.getContents(stack);
			if (map.size() == 1) {
				String element = (String) map.keySet().toArray()[0];
				IElementProvider provider = ElementRegistry.getProviderForElement(element);
				if (provider instanceof IngotProvider) {
					IngotProvider ingot = (IngotProvider) provider;
					ItemStack nStack = ingot.getMat().copy();
					int amt = (int) (map.get(element) / ingot.getAmt());
					nStack.stackSize = amt;
					
					ItemRegistry.metalLump.removeMetalFromLump(nStack, element, amt * ingot.getAmt());
					
					return nStack;
				}
			}
		}
		
		return ItemRegistry.metalLump.getHalf(stack);
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ItemRegistry.metalLump);
	}
}
