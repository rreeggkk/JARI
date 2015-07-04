package io.github.rreeggkk.jari.common.inventory.slot;

import io.github.rreeggkk.jari.common.entity.tile.TileEntityRTG;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class RTGSlot extends Slot {
	private TileEntityRTG rtg;

	public RTGSlot(TileEntityRTG rtg, int index, int x, int y) {
		super(rtg, index, x, y);
		this.rtg = rtg;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return rtg.isItemValidForSlot(0, stack);
	}
}
