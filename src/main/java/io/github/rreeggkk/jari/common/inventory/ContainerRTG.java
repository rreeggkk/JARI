package io.github.rreeggkk.jari.common.inventory;

import io.github.rreeggkk.jari.common.entity.tile.TileEntityRTG;
import io.github.rreeggkk.jari.common.inventory.slot.RTGSlot;
import io.github.rreeggkk.jari.common.item.ItemRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerRTG extends Container implements ITileContainer{
	public TileEntityRTG tile;
	private int lastEnergy, lastEpT;

	public ContainerRTG(InventoryPlayer player,
			TileEntityRTG tilee) {
		int playerInvOffX = 0;
		int playerInvOffY = 12;

		tile = tilee;
		addSlotToContainer(new RTGSlot(tilee, 0, 80, 36));

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(player, x, 8 + 18 * x + playerInvOffX,
					130 + playerInvOffY));
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + 18 * x
						+ playerInvOffX, 72 + y * 18 + playerInvOffY));
			}
		}

	}

	@Override
	public void addCraftingToCrafters(ICrafting crafter) {
		super.addCraftingToCrafters(crafter);
		crafter.sendProgressBarUpdate(this, 0, tile.getEnergy());
		crafter.sendProgressBarUpdate(this, 1, tile.getInfoEnergyPerTick());
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);

			if (lastEnergy != tile.getEnergy()) {
				icrafting.sendProgressBarUpdate(this, 0, tile.getEnergy());
			}
			if (lastEpT != tile.getInfoEnergyPerTick()) {
				icrafting.sendProgressBarUpdate(this, 1, tile.getInfoEnergyPerTick());
			}
		}

		lastEnergy = tile.getEnergy();
		lastEpT = tile.getInfoEnergyPerTick();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int val) {
		if (id == 0) {
			tile.setEnergy(val);
		}
		if (id == 1) {
			tile.setEnergyPerTick(val);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}

	/**
	 * Used to sync client to server Only occurs
	 */
	@Override
	public boolean enchantItem(EntityPlayer player, int action) {
		return super.enchantItem(player, action);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(slotNum);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (slotNum == 0) {
				if (!mergeItemStack(itemstack1, 1, 37, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (itemstack1.getItem() == ItemRegistry.metalLump
					&& slotNum >= 1
					&& slotNum < 37) {
				if (!mergeItemStack(itemstack1, 0, 1, false)) {
					return null;
				}
			} else if (slotNum >= 1 && slotNum < 10) {
				if (!mergeItemStack(itemstack1, 10, 37, false)) {
					return null;
				}
			} else if (slotNum >= 10 && slotNum < 37) {
				if (!mergeItemStack(itemstack1, 1, 10, false)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack1, 1, 37, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;

	}

	@Override
	public TileEntity getTileEntity() {
		return tile;
	}
}
