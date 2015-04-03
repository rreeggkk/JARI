package io.github.rreeggkk.jari.common.item;

/*
 * Base item class for getting standard things done with quickly.
 * Extend this for pretty much every item you make.
 */

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.item.Item;

public class ItemBase extends Item {

	public ItemBase(String unlocName) {
		super();

		setUnlocalizedName(ModInformation.ID + "." + unlocName);
		setCreativeTab(JARI.tabRreeactors);
		setTextureName(ModInformation.ID + ":" + unlocName);
	}
}
