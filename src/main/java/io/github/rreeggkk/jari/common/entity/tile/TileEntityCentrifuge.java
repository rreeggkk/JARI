package io.github.rreeggkk.jari.common.entity.tile;

import io.github.rreeggkk.jari.common.elements.ElementRegistry;
import io.github.rreeggkk.jari.common.elements.provider.IElementProvider;
import io.github.rreeggkk.jari.common.enums.RedstonePowerMode;
import io.github.rreeggkk.jari.common.item.ItemRegistry;
import io.github.rreeggkk.jari.common.util.ConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import org.apfloat.Apfloat;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.TileEnergyHandler;

public class TileEntityCentrifuge extends TileEnergyHandler implements
ISidedInventory, IRedstoneControllable,
IEnergyAccessable, ITextControllable{

	private ItemStack[] inventory;
	private int currentProcessEnergy, processStartEnergy;
	private Item lastItemInMachine;
	private int lastItemMetaInMachine;
	private RedstonePowerMode powerMode;
	private int elementIndex;
	private double percentPurity;

	public TileEntityCentrifuge() {
		super();
		inventory = new ItemStack[3];
		storage = new EnergyStorage(10000);
		powerMode = RedstonePowerMode.REQUIRED_OFF;
	}

	@Override
	public void updateEntity() {
		// worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
		// worldObj.getBlockMetadata(xCoord, yCoord, zCoord), 2);
		if (!worldObj.isRemote && isRedstoneModeActive()) {

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

	private boolean isRedstoneModeActive() {
		switch (powerMode) {
			case ALWAYS_ON:
				return true;
			case ALWAYS_OFF:
				return false;
			case REQUIRED_ON:
				return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord,
						zCoord);
			case REQUIRED_OFF:
				return !worldObj.isBlockIndirectlyGettingPowered(xCoord,
						yCoord, zCoord);
		}
		return false;
	}

	private int getMinEnergyPerTick() {
		return 5;
	}

	private int getMaxEnergyPerTick() {
		return 5 * 4;
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
		powerMode = RedstonePowerMode.getFromIndex(compound
				.getInteger("PowerMode"));
		elementIndex = ElementRegistry.getIndex(compound.getString("Element"));
		percentPurity = compound.getDouble("Purity");
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
		compound.setInteger("PowerMode", powerMode.getIndex());
		compound.setString("Element", ElementRegistry.getFromIndex(elementIndex));
		compound.setDouble("Purity", percentPurity);
	}

	public int processItem() {
		if (canProcess()) {
			if (inventory[0].getItem() == ItemRegistry.metalLump) {
				HashMap<String, Apfloat> cont = ItemRegistry.metalLump.getContents(inventory[0]);
				if (cont.size() <= 1) {
					return 0;
				}

				double totalMassOld = ItemRegistry.metalLump.getTotalMass(inventory[0]).doubleValue();

				ItemStack o1 = new ItemStack(ItemRegistry.metalLump);
				ItemStack o2 = new ItemStack(ItemRegistry.metalLump);
				
				String element = ElementRegistry.getFromIndex(elementIndex);

				double planAmt = cont.get(element).doubleValue();
				double total = planAmt/percentPurity;

				if (total <= totalMassOld) {
					double totalMassNew = totalMassOld - planAmt;
					double others = total-planAmt;

					for (String s : cont.keySet()) {
						if (s.equals(element)) {
							ItemRegistry.metalLump.addMetalToLump(o1, s, cont.get(s));
						} else {
							Apfloat o1Mass = cont.get(s).divide(new Apfloat(totalMassNew)).multiply(new Apfloat(others));
							ItemRegistry.metalLump.addMetalToLump(o1, s, o1Mass);
							ItemRegistry.metalLump.addMetalToLump(o2, s, cont.get(s).subtract(o1Mass));
						}
					}
				} else {
					Apfloat amtTransfer = new Apfloat((percentPurity*totalMassOld - planAmt)/(percentPurity - 1));
					for (String s : cont.keySet()) {
						if (!s.equals(element)) {
							ItemRegistry.metalLump.addMetalToLump(o1, s, cont.get(s));
						} else {
							//If the right element
							ItemRegistry.metalLump.addMetalToLump(o1, s, cont.get(s).subtract(amtTransfer));
							ItemRegistry.metalLump.addMetalToLump(o2, s, amtTransfer);
						}
					}
				}

				inventory[1] = o1;
				inventory[2] = o2;
				
				int pe = getProcessEnergy();

				--inventory[0].stackSize;

				if (inventory[0].stackSize <= 0) {
					inventory[0] = null;
				}

				return pe;
			}
		}
		return 0;
	}

	private int getProcessEnergy() {
		HashMap<String, Apfloat> cont = ItemRegistry.metalLump.getContents(inventory[0]);

		Apfloat mass = new Apfloat(0);
		Apfloat totalMass = new Apfloat(0);

		IElementProvider provider = ElementRegistry.getProviderForElement(ElementRegistry.getFromIndex(elementIndex));

		for (String s : cont.keySet()) {
			IElementProvider prov = ElementRegistry.getProviderForElement(s);
			if (provider.isSameElementAs(prov)) {
				mass = mass.add(cont.get(s));
			}
			totalMass = totalMass.add(cont.get(s));
		}

		double A = mass.doubleValue()/totalMass.doubleValue();
		double B = percentPurity;
		double D = A-B;
		double Q = Math.abs(D);

		//double numer = ConfigHandler.CENTRIFUGE_POWER_USE_MULTIPLIER * 100 * Math.pow(Q, 1/6f) * Math.log1p(totalMass);

		//double denom = Math.log(Math.log(1.7 - Q + 1) + 1);

		return (int) (ConfigHandler.CENTRIFUGE_POWER_USE_MULTIPLIER * 500 * Math.log(100 * Q + 1) + totalMass.doubleValue()/30);//(int) (numer/denom);
	}

	private boolean canProcess() {
		if (inventory[0] == null) {
			return false;
		} else {
			return canProcessItemstack(inventory[0]);
		}
	}

	private boolean canProcessItemstack(ItemStack stack) {
		if (stack == null) {
			return false;
		}

		HashMap<String, Apfloat> cont = ItemRegistry.metalLump.getContents(stack);
		if (cont.size() <= 1 || !cont.containsKey(ElementRegistry.getFromIndex(elementIndex))) {
			return false;
		}

		ArrayList<String> elements = new ArrayList<String>();
		for (String s : cont.keySet()) {
			IElementProvider provider = ElementRegistry.getProviderForElement(s);
			boolean notIn = true;
			for (String ele : elements) {
				if (provider.isSameElementAs(ElementRegistry.getProviderForElement(ele))) {
					notIn = false;
				}
			}
			if (notIn) {
				elements.add(s);
			}
		}

		return elements.size() == 1;
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
		return "tileEntity.rreeCentrifuge.name";
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
		if (slot == 0 && stack.getItem() == ItemRegistry.metalLump && canProcessItemstack(stack)) {
			return true;
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] {0,1,2};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return slot == 1 || slot == 2;
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
		return isRedstoneModeActive() && currentProcessEnergy != 0
				&& canProcess()
				&& storage.getEnergyStored() >= getCurrentEnergyPerTick();
	}

	public int getEnergyScaled(int maximum) {
		return (int) ((float) storage.getEnergyStored() * maximum / storage
				.getMaxEnergyStored());
	}

	public int getProgressScaled(int maximum) {
		int val = (int) ((float) currentProcessEnergy * maximum / processStartEnergy);

		return val;
	}

	public int getFlameScaled(int maximum) {
		return (int) ((float) getCurrentEnergyPerTick() * maximum / getMaxEnergyPerTick());
	}

	@Override
	public int getEnergy() {
		return storage.getEnergyStored();
	}

	@Override
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

	public int getComparatorOutput() {
		return getEnergyScaled(15);
	}

	@Override
	public RedstonePowerMode getPowerMode() {
		return powerMode;
	}

	@Override
	public void setPowerMode(RedstonePowerMode powerMode) {
		this.powerMode = powerMode;
	}

	@Override
	public String getString(int index) {
		if (index == 1) {
			return ElementRegistry.getFromIndex(elementIndex);
		} else if (index == 2) {
			return getPercent() + "%";
		}
		return "";
	}

	@Override
	public void setString(int index, String str) {
		if (index == 1) {
			//element = ELlemenstr;
		} else if (index == 2) {
			percentPurity = Integer.parseInt(str);
		}
	}

	public int getPercent() {
		return (int) (percentPurity * 100);
	}
	
	public void setPercent(int percentPurity) {
		percentPurity = Math.max(0, Math.min(100, percentPurity));
		this.percentPurity = percentPurity/100.0;
	}
	
	public void setElementIndex(int index) {
		elementIndex = index;
	}

	public int getElementIndex() {
		return elementIndex;
	}
}
