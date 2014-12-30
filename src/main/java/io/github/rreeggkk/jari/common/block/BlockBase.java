package io.github.rreeggkk.jari.common.block;

/*
* Base block class for getting standard things done with quickly.
* Extend this for pretty much every block you make.
*/

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {

	public BlockBase(String unlocName, Material material, SoundType soundType, float hardness) {
		super(material);

		setBlockName(ModInformation.ID + "." + unlocName);
		setCreativeTab(JARI.tabRreeactors);
		setStepSound(soundType);
		setHardness(hardness);
	}
}
