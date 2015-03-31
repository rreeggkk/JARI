package io.github.rreeggkk.jari.common.item;

import cpw.mods.fml.common.registry.GameRegistry;

/*
 * Class to register your blocks in.
 * The order that you list them here is the order they are registered.
 * Keep that in mind if you like nicely organized creative tabs.
 */


public class ItemRegistry {

	// Items
	public static ItemLumpOfMetal metalLump;

	public static void registerItems() {
		GameRegistry.registerItem(metalLump = new ItemLumpOfMetal(), "metalLump");
	}
}
