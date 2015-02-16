package io.github.rreeggkk.jari.common.inventory;

import io.github.rreeggkk.jari.common.crafting.hydraulic.HydraulicSeparatorCraftingHandler;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.enums.RedstonePowerMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerHydraulicSeparator extends Container {
	public TileEntityHydraulicSeparator tile;
	private int lastCook, lastEnergy, lastStartEnergy, lastWaterAmount, lastPowerIndex;

	public ContainerHydraulicSeparator(InventoryPlayer player,
			TileEntityHydraulicSeparator tilee) {
		int playerInvOffX = 0;
		int playerInvOffY = 12;

		tile = tilee;
		addSlotToContainer(new Slot(tilee, 0, 44, 36));
		addSlotToContainer(new SlotFurnace(player.player, tilee, 1, 116, 36));

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
		crafter.sendProgressBarUpdate(this, 3, tile.getWaterCount());
		crafter.sendProgressBarUpdate(this, 4, tile.getPowerMode().getIndex());
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
			if (lastWaterAmount != tile.getWaterCount()) {
				icrafting.sendProgressBarUpdate(this, 3, tile.getWaterCount());
			}
			if (lastPowerIndex != tile.getPowerMode().getIndex()) {
				icrafting.sendProgressBarUpdate(this, 4, tile.getPowerMode().getIndex());
			}
		}

		lastEnergy = tile.getEnergy();
		lastCook = tile.getProcess();
		lastStartEnergy = tile.getProcessStartEnergy();
		lastWaterAmount = tile.getWaterCount();
		lastPowerIndex = tile.getPowerMode().getIndex();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int val) {
		if (id == 0) tile.setEnergy(val);
		if (id == 1) tile.setProcess(val);
		if (id == 2) tile.setProcessStartEnergy(val);
		if (id == 3) tile.setWaterCount(val);
		if (id == 4) tile.setPowerMode(RedstonePowerMode.getFromIndex(val));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}
	
	/**
	 * Used to sync client->server
	 * Only occurs 
	 */
	@Override
	public boolean enchantItem(EntityPlayer player, int action) {
		//Make sure that only the server acts on the action
		if (!player.getEntityWorld().isRemote) {
			tile.setPowerMode(RedstonePowerMode.getNext(tile.getPowerMode()));
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
		Slot slot = (Slot)this.inventorySlots.get(slotNum);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (slotNum == 0 || slotNum == 1)
			{
				if (!this.mergeItemStack(itemstack1, 2, 38, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (HydraulicSeparatorCraftingHandler.instance.getRecipeForInput(itemstack1) != null && slotNum >= 2 && slotNum < 38) {
				if (!this.mergeItemStack(itemstack1, 0, 1, false))
				{
					return null;
				}				
			}
			else if (slotNum >= 2 && slotNum < 11)
			{
				if (!this.mergeItemStack(itemstack1, 11, 38, false))
				{
					return null;
				}
			}
			else if (slotNum >= 11 && slotNum < 38)
			{
				if (!this.mergeItemStack(itemstack1, 2, 11, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 2, 38, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;

	}
}
