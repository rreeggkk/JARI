package io.github.rreeggkk.reactors.common.util;

//This class handles all generation the mod contains.

import io.github.rreeggkk.reactors.common.block.BlockRegistry;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class GenerationHandler implements IWorldGenerator {

	// Which dimension to generate in by dimension ID (Nether -1, Overworld 0, End 1, etc)
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
			case -1:
				break;
			case 0:
				generateSurface(world, random, chunkX * 16, chunkZ * 16);
				break;
			case 1:
				break;
		}
	}

	//The actual generation method.
	private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
    }
}
