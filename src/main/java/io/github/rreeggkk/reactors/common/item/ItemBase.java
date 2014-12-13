package io.github.rreeggkk.reactors.common.item;

/*
* Base item class for getting standard things done with quickly.
* Extend this for pretty much every item you make.
*/

import io.github.rreeggkk.reactors.Rreeactors;
import io.github.rreeggkk.reactors.common.reference.ModInformation;
import net.minecraft.item.Item;

public class ItemBase extends Item {

	public ItemBase(String unlocName) {
		super();

		setUnlocalizedName(ModInformation.ID + "." + unlocName);
		setCreativeTab(Rreeactors.tabRreeactors);
	}
}
