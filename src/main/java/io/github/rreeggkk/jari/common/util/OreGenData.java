package io.github.rreeggkk.jari.common.util;

import net.minecraft.block.Block;

public class OreGenData {

	private int maxHeight = 0;
	private int minHeight = 0;
	private int orePerVein = 0;
	private int veinPerChunk = 0;
	private Block block;
	private EnabledType enabledType = EnabledType.DYNAMIC;

	public OreGenData() {
	}

	public OreGenData(int maxHeight, int minHeight, int orePerVein,
			int veinPerChunk, Block block) {
		this.maxHeight = maxHeight;
		this.minHeight = minHeight;
		this.orePerVein = orePerVein;
		this.veinPerChunk = veinPerChunk;
		this.block = block;
	}

	public OreGenData(int maxHeight, int minHeight, int orePerVein,
			int veinPerChunk, Block block, EnabledType enabledType) {
		this.maxHeight = maxHeight;
		this.minHeight = minHeight;
		this.orePerVein = orePerVein;
		this.veinPerChunk = veinPerChunk;
		this.block = block;
		this.enabledType = enabledType;
	}

	public EnabledType getEnabledType() {
		return enabledType;
	}

	public void setEnabledType(EnabledType enabledType) {
		this.enabledType = enabledType;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	public int getOrePerVein() {
		return orePerVein;
	}

	public void setOrePerVein(int orePerVein) {
		this.orePerVein = orePerVein;
	}

	public int getVeinPerChunk() {
		return veinPerChunk;
	}

	public void setVeinPerChunk(int veinPerChunk) {
		this.veinPerChunk = veinPerChunk;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public enum EnabledType {
		DISABLED, DYNAMIC, ALWAYS_ENABLED;

		public static String[] getStringValues() {
			String[] result = new String[values().length];

			for (int i = 0; i < result.length; i++) {
				result[i] = values()[i].toString();
			}

			return result;
		}
	}
}
