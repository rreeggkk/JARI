package io.github.rreeggkk.jari.common.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MachineSlot extends Slot {
	private IInventory inv;

	public MachineSlot(IInventory rtg, int slotIndex, int x, int y) {
		super(rtg, slotIndex, x, y);
		this.inv = rtg;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return inv.isItemValidForSlot(getSlotIndex(), stack);
	}
}
