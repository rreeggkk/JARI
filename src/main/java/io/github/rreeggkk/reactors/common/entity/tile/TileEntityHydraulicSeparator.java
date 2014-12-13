package io.github.rreeggkk.reactors.common.entity.tile;

import io.github.rreeggkk.reactors.common.crafting.hydraulic.HydraulicSeparatorCraftingHandler;
import io.github.rreeggkk.reactors.common.crafting.hydraulic.IHydraulicRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.TileEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityHydraulicSeparator extends TileEnergyHandler implements ISidedInventory{
	private ItemStack[] inventory;
   /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    //public int currentItemBurnTime;
    /** The number of ticks that the current item has been cooking for */
    private int currentProcessEnergy = 0, processStartEnergy = 0;
    private Item lastItemInMachine;
    private int lastItemMetaInMachine;

	public TileEntityHydraulicSeparator(){
		super();
		inventory = new ItemStack[2];
		storage = new EnergyStorage(100000);
	}
	
	@Override
	public void updateEntity() {
		//worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), 2);   		
		if (!this.worldObj.isRemote) {
			
			if (inventory[0] != null && (inventory[0].getItem() != lastItemInMachine || inventory[0].getItemDamage() != lastItemMetaInMachine)) {
				processStartEnergy = processItem();
				currentProcessEnergy = processStartEnergy;
				
				lastItemInMachine = inventory[0].getItem();
				lastItemMetaInMachine = inventory[0].getItemDamage();
			}
			
			int energyUsage = getCurrentEnergyPerTick();
			
			if (currentProcessEnergy < energyUsage) {
				energyUsage = currentProcessEnergy;
			}
			
			if (storage.getEnergyStored() - energyUsage > 0 && currentProcessEnergy > 0 && canProcess()) {
				storage.setEnergyStored(storage.getEnergyStored() - energyUsage);
				currentProcessEnergy -= energyUsage;
				if (!isOn(worldObj, xCoord, yCoord, zCoord)) {
		    		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) + 1, 2);   		
				}
			} else {
				if (isOn(worldObj, xCoord, yCoord, zCoord)) {
		    		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord) - 1, 2);   		
				}

			}
			if (currentProcessEnergy <= 0){
				processStartEnergy = processItem();
				currentProcessEnergy = processStartEnergy;
			}
		}
	}
	
	private int getMinEnergyPerTick() {
		return 80;
	}
	private int getMaxEnergyPerTick() {
		return 80*4;
	}
	private int getCurrentEnergyPerTick() {
		int energyUsage = (int)(storage.getEnergyStored() / 50f);
		
		if (energyUsage < getMinEnergyPerTick()) {
			energyUsage = 0;
		}
		if (energyUsage > getMaxEnergyPerTick()) {
			energyUsage = getMaxEnergyPerTick();
		}
		return energyUsage;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		this.inventory = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.inventory.length) {
				this.inventory[b0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		currentProcessEnergy = compound.getInteger("Progress");
		processStartEnergy = compound.getInteger("Start");
	}

	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.inventory.length; ++i) {
			if (this.inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		compound.setTag("Items", nbttaglist);

		compound.setInteger("Progress", currentProcessEnergy);
		compound.setInteger("Start", processStartEnergy);
	}
	
	
	
	/**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public int processItem()
    {
        if (this.canProcess())
        {
        	IHydraulicRecipe r = HydraulicSeparatorCraftingHandler.instance.getRecipeForInput(this.inventory[0]);
        	
            ItemStack itemstack = r.getResult(this.inventory[0]);

            if (this.inventory[1] == null)
            {
                this.inventory[1] = itemstack.copy();
            }
            else if (this.inventory[1].getItem() == itemstack.getItem())
            {
                this.inventory[1].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.inventory[0].stackSize;

            if (this.inventory[0].stackSize <= 0)
            {
                this.inventory[0] = null;
            }
            
            return r.getRequiredEnergy();
        }
        return 0;
    }
    
    private int getProcessEnergy() {
    	IHydraulicRecipe r = HydraulicSeparatorCraftingHandler.instance.getRecipeForInput(this.inventory[0]);
    	
    	if (r == null) {
    		return 0;
    	}
    	
    	return r.getRequiredEnergy();
    }

    private boolean canProcess()
    {
        if (this.inventory[0] == null)
        {
            return false;
        }
        else
        {
        	IHydraulicRecipe r = HydraulicSeparatorCraftingHandler.instance.getRecipeForInput(this.inventory[0]);
        	
        	if (r == null) {
        		return false;
        	}
        	
            ItemStack itemstack = r.getResult(this.inventory[0]);
            if (itemstack == null) return false;
            if (this.inventory[1] == null) return true;
            if (!this.inventory[1].isItemEqual(itemstack)) return false;
            int result = inventory[1].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.inventory[1].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int numOfItems) {
		if (this.inventory[slot] != null) {
			ItemStack itemstack;

			if (this.inventory[slot].stackSize <= numOfItems) {
				itemstack = this.inventory[slot];
				this.inventory[slot] = null;
				return itemstack;
			} else {
				itemstack = this.inventory[slot].splitStack(numOfItems);

				if (this.inventory[slot].stackSize == 0) {
					this.inventory[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.inventory[slot] != null) {
			ItemStack itemstack = this.inventory[slot];
			this.inventory[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.inventory[slot] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
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
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : player.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 0){
			return true;
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] {(side == 0 ? 1 : 0)};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
        return slot == 1;
	}

	public boolean isOn(World world, int x, int y, int z){
		return isOn(world.getBlockMetadata(x, y, z));
	}
	public boolean isOn(int meta){
		if (meta % 2 == 0){
			return false;
		}
		return true;
	}

	public boolean isRunning() {
		return (currentProcessEnergy != 0) && canProcess() && storage.getEnergyStored() >= getCurrentEnergyPerTick();
	}

	@SideOnly(Side.CLIENT)
	public int getEnergyScaled(int maximum) {
		return (int)((float)storage.getEnergyStored() * maximum/(float)storage.getMaxEnergyStored());
	}
	@SideOnly(Side.CLIENT)
	public int getProgressScaled(int maximum) {
		int val = (int) ((float)currentProcessEnergy * maximum / (float)processStartEnergy);
		
		return val;
	}
	@SideOnly(Side.CLIENT)
	public int getFlameScaled(int maximum) {
		return (int)((((float)getCurrentEnergyPerTick() / (float)getMaxEnergyPerTick())) * maximum);
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
}
