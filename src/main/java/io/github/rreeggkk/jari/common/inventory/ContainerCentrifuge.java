package io.github.rreeggkk.jari.common.inventory;

import io.github.rreeggkk.jari.common.elements.ElementRegistry;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityCentrifuge;
import io.github.rreeggkk.jari.common.enums.RedstonePowerMode;
import io.github.rreeggkk.jari.common.inventory.slot.MachineSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCentrifuge extends Container implements ITileContainer{
	public TileEntityCentrifuge tile;
	private int lastCook, lastEnergy, lastStartEnergy,
			lastPowerIndex, lastElement, lastPercent;

	public ContainerCentrifuge(InventoryPlayer player,
			TileEntityCentrifuge tilee) {
		int playerInvOffX = 0;
		int playerInvOffY = 12;

		tile = tilee;
		addSlotToContainer(new MachineSlot(tilee, 0, 44, 36));
		addSlotToContainer(new SlotFurnace(player.player, tilee, 1, 116, 27));
		addSlotToContainer(new SlotFurnace(player.player, tilee, 2, 116, 27 + 18));

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
		crafter.sendProgressBarUpdate(this, 1, tile.getProcess());
		crafter.sendProgressBarUpdate(this, 2, tile.getProcessStartEnergy());
		crafter.sendProgressBarUpdate(this, 4, tile.getPowerMode().getIndex());
		crafter.sendProgressBarUpdate(this, 5, tile.getElementIndex());
		crafter.sendProgressBarUpdate(this, 6, tile.getPercent());
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
			if (lastCook != tile.getProcess()) {
				icrafting.sendProgressBarUpdate(this, 1, tile.getProcess());
			}
			if (lastStartEnergy != tile.getProcessStartEnergy()) {
				icrafting.sendProgressBarUpdate(this, 2,
						tile.getProcessStartEnergy());
			}
			if (lastPowerIndex != tile.getPowerMode().getIndex()) {
				icrafting.sendProgressBarUpdate(this, 4, tile.getPowerMode()
						.getIndex());
			}
			if (lastElement != tile.getElementIndex()) {
				icrafting.sendProgressBarUpdate(this, 5, tile.getElementIndex());
			}
			if (lastPercent != tile.getPercent()) {
				icrafting.sendProgressBarUpdate(this, 6, tile.getPercent());
			}
		}

		lastEnergy = tile.getEnergy();
		lastCook = tile.getProcess();
		lastStartEnergy = tile.getProcessStartEnergy();
		lastPowerIndex = tile.getPowerMode().getIndex();
		lastElement = tile.getElementIndex();
		lastPercent = tile.getPercent();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int val) {
		if (id == 0) {
			tile.setEnergy(val);
		}
		if (id == 1) {
			tile.setProcess(val);
		}
		if (id == 2) {
			tile.setProcessStartEnergy(val);
		}
		if (id == 4) {
			tile.setPowerMode(RedstonePowerMode.getFromIndex(val));
		}
		if (id == 5) {
			tile.setElementIndex(val);
		}
		if (id == 6) {
			tile.setPercent(val);
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
		// Make sure that only the server acts on the action
		if (!player.getEntityWorld().isRemote) {
			if (action == 0) {
				tile.setPowerMode(RedstonePowerMode.getNext(tile.getPowerMode()));
			} else if (action == 1) {
				tile.setElementIndex((tile.getElementIndex() + 1)%ElementRegistry.getCount());
			} else if (action == 2) {
				tile.setPercent(tile.getPercent()-10);
			} else if (action == 3) {
				tile.setPercent(tile.getPercent()-5);
			} else if (action == 4) {
				tile.setPercent(tile.getPercent()-1);
			} else if (action == 5) {
				tile.setPercent(tile.getPercent()+1);
			} else if (action == 6) {
				tile.setPercent(tile.getPercent()+5);
			} else if (action == 7) {
				tile.setPercent(tile.getPercent()+10);
			} else if (action == 8) {
				tile.setElementIndex((tile.getElementIndex() - 1 + ElementRegistry.getCount())%ElementRegistry.getCount());
			}
		}
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
			if (slotNum == 0 || slotNum == 1 || slotNum == 2) {
				if (!mergeItemStack(itemstack1, 3, 39, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (tile.isItemValidForSlot(0, itemstack1)
					&& slotNum >= 3
					&& slotNum < 39) {
				if (!mergeItemStack(itemstack1, 0, 1, false)) {
					return null;
				}
			} else if (slotNum >= 3 && slotNum < 12) {
				if (!mergeItemStack(itemstack1, 12, 39, false)) {
					return null;
				}
			} else if (slotNum >= 12 && slotNum < 39) {
				if (!mergeItemStack(itemstack1, 3, 12, false)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack1, 3, 39, false)) {
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
