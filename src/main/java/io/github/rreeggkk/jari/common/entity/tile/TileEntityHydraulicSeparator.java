package io.github.rreeggkk.jari.common.entity.tile;

import io.github.rreeggkk.jari.common.crafting.hydraulic.HydraulicSeparatorCraftingHandler;
import io.github.rreeggkk.jari.common.crafting.hydraulic.IHydraulicRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.TileEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityHydraulicSeparator extends TileEnergyHandler implements
		ISidedInventory, IFluidHandler {
	public static final int maxWater = 16 * 1000;

	private FluidTank tank;

	private ItemStack[] inventory;
	private int currentProcessEnergy = 0, processStartEnergy = 0;
	private Item lastItemInMachine;
	private int lastItemMetaInMachine;

	public TileEntityHydraulicSeparator() {
		super();
		inventory = new ItemStack[2];
		storage = new EnergyStorage(100000);
		tank = new FluidTank(maxWater);
	}

	@Override
	public void updateEntity() {
		// worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
		// worldObj.getBlockMetadata(xCoord, yCoord, zCoord), 2);
		if (!worldObj.isRemote) {

			if (inventory[0] != null
					&& (inventory[0].getItem() != lastItemInMachine || inventory[0]
							.getItemDamage() != lastItemMetaInMachine)) {
				processStartEnergy = getProcessEnergy();
				currentProcessEnergy = processStartEnergy;

				lastItemInMachine = inventory[0].getItem();
				lastItemMetaInMachine = inventory[0].getItemDamage();
			}

			int energyUsage = getCurrentEnergyPerTick();

			if (currentProcessEnergy < energyUsage) {
				energyUsage = currentProcessEnergy;
			}

			if (storage.getEnergyStored() - energyUsage > 0
					&& currentProcessEnergy > 0 && canProcess()) {
				storage.setEnergyStored(storage.getEnergyStored() - energyUsage);
				currentProcessEnergy -= energyUsage;
				if (!isOn(worldObj, xCoord, yCoord, zCoord)) {
					worldObj.setBlockMetadataWithNotify(
							xCoord,
							yCoord,
							zCoord,
							worldObj.getBlockMetadata(xCoord, yCoord, zCoord) + 1,
							2);
				}
			} else {
				if (isOn(worldObj, xCoord, yCoord, zCoord)) {
					worldObj.setBlockMetadataWithNotify(
							xCoord,
							yCoord,
							zCoord,
							worldObj.getBlockMetadata(xCoord, yCoord, zCoord) - 1,
							2);
				}

			}
			if (currentProcessEnergy <= 0) {
				processStartEnergy = processItem();
				currentProcessEnergy = processStartEnergy;
			}
		}
	}

	private int getMinEnergyPerTick() {
		return 80;
	}

	private int getMaxEnergyPerTick() {
		return 80 * 4;
	}

	private int getCurrentEnergyPerTick() {
		int energyUsage = (int) (storage.getEnergyStored() / 50f);

		if (energyUsage < getMinEnergyPerTick()) {
			energyUsage = 0;
		}
		if (energyUsage > getMaxEnergyPerTick()) {
			energyUsage = getMaxEnergyPerTick();
		}
		return energyUsage;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		inventory = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < inventory.length) {
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		currentProcessEnergy = compound.getInteger("Progress");
		processStartEnergy = compound.getInteger("Start");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i) {
			if (inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		compound.setTag("Items", nbttaglist);

		compound.setInteger("Progress", currentProcessEnergy);
		compound.setInteger("Start", processStartEnergy);
	}

	public int processItem() {
		if (canProcess()) {
			IHydraulicRecipe r = HydraulicSeparatorCraftingHandler.instance
					.getRecipeForInput(inventory[0]);

			ItemStack itemstack = r.getResult(inventory[0]);

			if (inventory[1] == null) {
				inventory[1] = itemstack.copy();
			} else if (inventory[1].getItem() == itemstack.getItem()) {
				inventory[1].stackSize += itemstack.stackSize; // Forge BugFix:
																// Results may
																// have multiple
																// items
			}

			--inventory[0].stackSize;

			if (inventory[0].stackSize <= 0) {
				inventory[0] = null;
			}

			tank.drain(r.getRequiredWater(), true);

			return r.getRequiredEnergy();
		}
		return 0;
	}

	private int getProcessEnergy() {
		IHydraulicRecipe r = HydraulicSeparatorCraftingHandler.instance
				.getRecipeForInput(inventory[0]);

		if (r == null) {
			return 0;
		}

		return r.getRequiredEnergy();
	}

	private boolean canProcess() {
		if (inventory[0] == null) {
			return false;
		} else {
			IHydraulicRecipe r = HydraulicSeparatorCraftingHandler.instance
					.getRecipeForInput(inventory[0]);

			if (r == null) {
				return false;
			}

			if (tank.getFluidAmount() < r.getRequiredWater()) {
				return false;
			}

			ItemStack itemstack = r.getResult(inventory[0]);
			if (itemstack == null) {
				return false;
			}
			if (inventory[1] == null) {
				return true;
			}
			if (!inventory[1].isItemEqual(itemstack)) {
				return false;
			}
			int result = inventory[1].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit()
					&& result <= inventory[1].getMaxStackSize(); // Forge
																	// BugFix:
																	// Make it
																	// respect
																	// stack
																	// sizes
																	// properly.
		}
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int numOfItems) {
		if (inventory[slot] != null) {
			ItemStack itemstack;

			if (inventory[slot].stackSize <= numOfItems) {
				itemstack = inventory[slot];
				inventory[slot] = null;
				return itemstack;
			} else {
				itemstack = inventory[slot].splitStack(numOfItems);

				if (inventory[slot].stackSize == 0) {
					inventory[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (inventory[slot] != null) {
			ItemStack itemstack = inventory[slot];
			inventory[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		inventory[slot] = itemstack;

		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "tileEntity.rreeHydraulicSeparator.name";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false
				: player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D,
						zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { side == 0 ? 1 : 0 };
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return slot == 1;
	}

	public boolean isOn(World world, int x, int y, int z) {
		return isOn(world.getBlockMetadata(x, y, z));
	}

	public boolean isOn(int meta) {
		if (meta % 2 == 0) {
			return false;
		}
		return true;
	}

	public boolean isRunning() {
		return currentProcessEnergy != 0 && canProcess()
				&& storage.getEnergyStored() >= getCurrentEnergyPerTick();
	}

	@SideOnly(Side.CLIENT)
	public int getEnergyScaled(int maximum) {
		return (int) ((float) storage.getEnergyStored() * maximum / storage
				.getMaxEnergyStored());
	}

	@SideOnly(Side.CLIENT)
	public int getProgressScaled(int maximum) {
		int val = (int) ((float) currentProcessEnergy * maximum / processStartEnergy);

		return val;
	}

	@SideOnly(Side.CLIENT)
	public int getFlameScaled(int maximum) {
		return (int) ((float) getCurrentEnergyPerTick() * maximum / getMaxEnergyPerTick());
	}

	@SideOnly(Side.CLIENT)
	public int getWaterScaled(int maximum) {
		return (int) ((float) tank.getFluidAmount() * maximum / maxWater);
	}

	public int getWaterCount() {
		return tank.getFluidAmount();
	}

	public void setWaterCount(int i) {
		tank.fill(
				new FluidStack(FluidRegistry.WATER, i - tank.getFluidAmount()),
				true);
	}

	public FluidTank getTank() {
		return tank;
	}

	public void setTank(FluidTank tank) {
		this.tank = tank;
	}

	public int getEnergy() {
		return storage.getEnergyStored();
	}

	public int getMaxEnergy() {
		return storage.getMaxEnergyStored();
	}

	public int getProcess() {
		return currentProcessEnergy;
	}

	public void setEnergy(int energy) {
		storage.setEnergyStored(energy);
	}

	public void setProcess(int num) {
		currentProcessEnergy = num;
	}

	public int getProcessStartEnergy() {
		return processStartEnergy;
	}

	public void setProcessStartEnergy(int processStartEnergy) {
		this.processStartEnergy = processStartEnergy;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid == FluidRegistry.WATER;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { tank.getInfo() };
	}
}
