package io.github.rreeggkk.jari.common.entity.tile;

import io.github.rreeggkk.jari.common.elements.ElementRegistry;
import io.github.rreeggkk.jari.common.elements.FissionMode;
import io.github.rreeggkk.jari.common.elements.provider.IElementProvider;
import io.github.rreeggkk.jari.common.item.ItemRegistry;
import io.github.rreeggkk.jari.common.util.ConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.tileentity.IEnergyInfo;

public class TileEntityRTG extends TileEntity implements ISidedInventory, IEnergyProvider, IEnergyInfo, IEnergyAccessable{

	private ItemStack[] inventory;
	private EnergyStorage energyStorage;
	private int lastEnergyPerTick;
	private double energyCounter;

	public TileEntityRTG() {
		energyStorage = new EnergyStorage(10000, 1000000, 10000);
		inventory = new ItemStack[1];
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {

			double energyGenerated = 0;
			if (inventory[0] != null) {
				HashMap<String, Double> metalMap = ItemRegistry.metalLump.getContents(inventory[0]);
				if (metalMap != null) {
					for (String element : metalMap.keySet()) {
						double mass = metalMap.get(element);
						IElementProvider provider = ElementRegistry.getProviderForElement(element);
						if (provider != null) {
							double gramsFiss = provider.getSpontaneousFissionChance() * mass;
							
							if (gramsFiss > mass) {
								gramsFiss = mass;
							}

							energyGenerated += provider.getFissionEnergy() * gramsFiss;

							Map<String, Double> fissProd = provider.doFission(FissionMode.DECAY, gramsFiss);
							
							ItemRegistry.metalLump.removeMetalFromLump(inventory[0], element, gramsFiss);
							
							for (String newElement : fissProd.keySet()) {
								ItemRegistry.metalLump.addMetalToLump(inventory[0], newElement, fissProd.get(newElement));
							}
						}
					}
				}
			}

			energyGenerated *= ConfigHandler.RTGEnergyMultiplier;

			lastEnergyPerTick = (int)energyGenerated;
			
			energyCounter += (energyGenerated - lastEnergyPerTick);
			if (energyCounter >= 1) {
				energyCounter-=1;
				lastEnergyPerTick+=1;
			}

			energyStorage.receiveEnergy(lastEnergyPerTick, false);
			
			ArrayList<ForgeDirection> outputs = new ArrayList<ForgeDirection>();
			for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
				int nX = xCoord + fd.offsetX;
				int nY = yCoord + fd.offsetY;
				int nZ = zCoord + fd.offsetZ;
				
				Block b = worldObj.getBlock(nX, nY, nZ);
				
				if (b.hasTileEntity(b.getDamageValue(worldObj, nX, nY, nZ))) {
					TileEntity te = worldObj.getTileEntity(nX, nY, nZ);
					if (te instanceof IEnergyHandler) {
						IEnergyHandler eh = (IEnergyHandler)te;
						if (eh.canConnectEnergy(fd.getOpposite())) {
							outputs.add(fd);
						}
					}
				}
			}
			for (int i = 0; i<outputs.size(); i++) {
				ForgeDirection fd = outputs.get(i);
				
				int amtToSend = energyStorage.getEnergyStored()/(outputs.size()-i);
				
				int nX = xCoord + fd.offsetX;
				int nY = yCoord + fd.offsetY;
				int nZ = zCoord + fd.offsetZ;
				
				Block b = worldObj.getBlock(nX, nY, nZ);
				
				if (b.hasTileEntity(b.getDamageValue(worldObj, nX, nY, nZ))) {
					TileEntity te = worldObj.getTileEntity(nX, nY, nZ);
					if (te instanceof IEnergyHandler) {
						IEnergyHandler eh = (IEnergyHandler)te;
						int amtSent = eh.receiveEnergy(fd.getOpposite(), amtToSend, false);
						energyStorage.extractEnergy(amtSent, false);
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		energyStorage.readFromNBT(nbt);

		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		inventory = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < inventory.length) {
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		
		lastEnergyPerTick = nbt.getInteger("LastNRG");
		energyCounter = nbt.getDouble("enCounter");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);
		
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i) {
			if (inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);

		nbt.setInteger("LastNRG", lastEnergyPerTick);
		nbt.setDouble("enCounter", energyCounter);
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
	public ItemStack decrStackSize(int slot, int num) {
		if (num > inventory[slot].stackSize) {
			num = inventory[slot].stackSize;
		}

		ItemStack s = inventory[slot].copy();
		s.stackSize = num;

		inventory[slot].stackSize -= num;

		return s;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return inventory[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
	}

	@Override
	public String getInventoryName() {
		return "tileEntity.rreeRTG.name";
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
		if (stack.getItem() == ItemRegistry.metalLump) {
			return ItemRegistry.metalLump.getTotalMass(stack) <= ConfigHandler.RTG_MAX_WEIGHT;
		}
		return false;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}

	@Override
	public int getInfoEnergyPerTick() {
		return lastEnergyPerTick;
	}

	@Override
	public int getInfoEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return energyStorage.getMaxExtract();
	}

	@Override
	public int getInfoMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public int extractEnergy(ForgeDirection dir, int amt, boolean simulate) {
		return energyStorage.extractEnergy(amt, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item,int side) {
		return isItemValidForSlot(slot, item);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item,
			int side) {
		return true;
	}

	public int getComparatorOutput() {
		return 0;
	}

	@Override
	public int getEnergy() {
		return energyStorage.getEnergyStored();
	}

	public void setEnergy(int val) {
		energyStorage.setEnergyStored(val);
	}

	@Override
	public int getMaxEnergy() {
		return energyStorage.getMaxEnergyStored();
	}

	public void setEnergyPerTick(int val) {
		lastEnergyPerTick = val;
	}
}
