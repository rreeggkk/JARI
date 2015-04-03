package io.github.rreeggkk.jari.common.block;

// General place to register all your blocks.

import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegistry {

	// Blocks
	public static BlockHydraulicSeparator blockHydraulicSeparator;
	public static BlockBase blockRadioactiveStone;

	public static void registerBlocks() {
		{// Hydraulic Separator
			GameRegistry.registerBlock(
					blockHydraulicSeparator = new BlockHydraulicSeparator(),
					"blockHydraulicSeparator");
			GameRegistry.registerTileEntity(TileEntityHydraulicSeparator.class,
					"tileHydraulicSeparator");
		}
		{// Radioactive Stone
			GameRegistry.registerBlock(blockRadioactiveStone = new BlockBase(
					"blockRadioactiveStone", Material.rock,
					Block.soundTypePiston, 2f), "blockRadioactiveStone");
			blockRadioactiveStone.setResistance(11f).setHarvestLevel("pickaxe",
					1);
			;
			blockRadioactiveStone.setBlockTextureName("minecraft:stone");
			blockRadioactiveStone.setLightLevel(4f / 15f);
			// blockRadioactiveStone.setLightLevel(1);
		}
	}
}
