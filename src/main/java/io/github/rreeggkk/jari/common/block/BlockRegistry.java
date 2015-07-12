package io.github.rreeggkk.jari.common.block;

// General place to register all your blocks.

import io.github.rreeggkk.jari.common.entity.tile.TileEntityCentrifuge;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityChemicalSeparator;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityRTG;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegistry {

	// Blocks
	public static BlockHydraulicSeparator blockHydraulicSeparator;
	public static BlockChemicalSeparator blockChemicalSeparator;
	public static BlockCentrifuge blockCentrifuge;
	public static BlockRTG blockRTG;
	public static BlockBase blockRadioactiveStone;

	public static void registerBlocks() {
		{// Hydraulic Separator
			GameRegistry.registerBlock(
					blockHydraulicSeparator = new BlockHydraulicSeparator(),
					"blockHydraulicSeparator");
			GameRegistry.registerTileEntity(TileEntityHydraulicSeparator.class,
					"tileHydraulicSeparator");
		}
		{// RTG
			GameRegistry.registerBlock(
					blockRTG = new BlockRTG(),
					"blockRTG");
			GameRegistry.registerTileEntity(TileEntityRTG.class,
					"blockRTG");
		}
		{// Chemical Separator
			GameRegistry.registerBlock(
					blockChemicalSeparator = new BlockChemicalSeparator(),
					"blockChemicalSeparator");
			GameRegistry.registerTileEntity(TileEntityChemicalSeparator.class,
					"tileChemicalSeparator");
		}
		{// Centrifuge
			GameRegistry.registerBlock(
					blockCentrifuge = new BlockCentrifuge(),
					"blockCentrifuge");
			GameRegistry.registerTileEntity(TileEntityCentrifuge.class,
					"tileCentrifuge");
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
