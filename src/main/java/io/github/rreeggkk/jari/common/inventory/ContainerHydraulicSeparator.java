package io.github.rreeggkk.jari.common.inventory;

import codechicken.core.fluid.FluidUtils;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerHydraulicSeparator extends Container{
	public TileEntityHydraulicSeparator tile;
	private int lastCook, lastEnergy, lastStartEnergy, lastWaterAmount;

	public ContainerHydraulicSeparator(InventoryPlayer player, TileEntityHydraulicSeparator tilee){
		int playerInvOffX = 0;
		int playerInvOffY = 12;

		this.tile = tilee;
		this.addSlotToContainer(new Slot(tilee, 0, 44, 36));
		this.addSlotToContainer(new SlotFurnace(player.player,tilee, 1, 116, 36));

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(player, x, 8 + 18 * x + playerInvOffX, 130 + playerInvOffY));
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + 18 * x + playerInvOffX, 72 + y * 18 + playerInvOffY));
			}
		}

	}
	public void addCraftingToCrafters(ICrafting crafter) {
		super.addCraftingToCrafters(crafter);
		crafter.sendProgressBarUpdate(this, 0, this.tile.getEnergy());
		crafter.sendProgressBarUpdate(this, 1, this.tile.getProcess());
		crafter.sendProgressBarUpdate(this, 2, this.tile.getProcessStartEnergy());
		crafter.sendProgressBarUpdate(this, 3, this.tile.getWaterCount());
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.lastEnergy != this.tile.getEnergy())
			{
				icrafting.sendProgressBarUpdate(this, 0, this.tile.getEnergy());
			}
			if (this.lastCook != this.tile.getProcess())
			{
				icrafting.sendProgressBarUpdate(this, 1, this.tile.getProcess());
			}
			if (this.lastStartEnergy != this.tile.getProcessStartEnergy())
			{
				icrafting.sendProgressBarUpdate(this, 2, this.tile.getProcessStartEnergy());
			}
			if (this.lastWaterAmount != this.tile.getWaterCount())
			{
				icrafting.sendProgressBarUpdate(this, 3, this.tile.getWaterCount());
			}
		}

		this.lastEnergy = this.tile.getEnergy();
		this.lastCook = this.tile.getProcess();
		this.lastStartEnergy = this.tile.getProcessStartEnergy();
		this.lastWaterAmount = this.tile.getWaterCount();
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int val)
	{
		if (id == 0)
		{
			this.tile.setEnergy(val);
		}
		if (id == 1)
		{
			this.tile.setProcess(val);
		}
		if (id == 2)
		{
			this.tile.setProcessStartEnergy(val);
		}
		if (id == 3)
		{
			this.tile.setWaterCount(val);
		}
	}

	public boolean canInteractWith(EntityPlayer player)
	{
		return this.tile.isUseableByPlayer(player);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){
		return null;
	}
}
