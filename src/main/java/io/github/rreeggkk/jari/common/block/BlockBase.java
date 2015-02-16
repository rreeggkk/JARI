package io.github.rreeggkk.jari.common.block;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * A base for every block
 * @author rreeggkk
 *
 */
public class BlockBase extends Block {

	/**
	 * 
	 * @param unlocName the unlocalized name for the block
	 * @param material the material the block is made of
	 * @param soundType the sound the block makes when you step on it
	 * @param hardness how hard it is (break speed)
	 */
	public BlockBase(String unlocName, Material material, SoundType soundType, float hardness) {
		super(material);

		//Set the variables corresponding to the above parameters
		setBlockName(ModInformation.ID + "." + unlocName);
		setCreativeTab(JARI.tabRreeactors);
		setStepSound(soundType);
		setHardness(hardness);
	}
}
