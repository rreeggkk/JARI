package io.github.rreeggkk.jari.common.util;

/*
 * Class for most of your events to be registered in.
 * Remember that there are two different registries for Events. This one will not work for everything.
 */

import java.util.HashMap;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.crafting.metal.MetalHalving;
import io.github.rreeggkk.jari.common.elements.ElementRegistry;
import io.github.rreeggkk.jari.common.elements.provider.IElementProvider;
import io.github.rreeggkk.jari.common.elements.provider.IngotProvider;
import io.github.rreeggkk.jari.common.item.ItemRegistry;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class EventHandler {

	public EventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void onConfigChanged(
			ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.modID.equals(ModInformation.ID)) {
			ConfigHandler.syncConfig();
			JARI.logger.info(TextHelper.localize("info." + ModInformation.ID
					+ ".console.config.refresh"));
		}
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event){

		if (metalIngot(event))
			return;
		else if (metalHalving(event))
			return;
		else
			return;

	}

	private boolean metalIngot(ItemCraftedEvent event) {
		
		boolean notFound = true;
		
		for (ItemStack mat : IngotProvider.getMats()) {
			if (mat.getItem() == event.crafting.getItem() && mat.getItemDamage() == event.crafting.getItemDamage()) {
				notFound = false;
				break;
			}
		}
		if (notFound) {
			return false;
		}
		
		int amountMetal = 0;
		int slot = -1;

		for (int x = 0; x < event.craftMatrix.getSizeInventory(); x++) {
			if (event.craftMatrix.getStackInSlot(x) != null) {
				if (event.craftMatrix.getStackInSlot(x).getItem() == ItemRegistry.metalLump) {
					amountMetal++;
					slot = x;
					if (amountMetal > 1) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		
		if (amountMetal != 1) {
			return false;
		}

		if (MetalHalving.instance.isConvertToIngot(event.craftMatrix.getStackInSlot(slot))) {
			ItemStack newStack = event.craftMatrix.getStackInSlot(slot).copy();
			
			int newStackSize = 1;

			HashMap<String, Double> map = ItemRegistry.metalLump.getContents(newStack);
			if (map.size() == 1) {
				String element = (String) map.keySet().toArray()[0];
				IElementProvider provider = ElementRegistry.getProviderForElement(element);
				if (provider instanceof IngotProvider) {
					IngotProvider ingot = (IngotProvider) provider;
					double amt = (map.get(element) % ingot.getAmt());
					
					if (amt > 0) {
						newStackSize = 2;
					}

					ItemRegistry.metalLump.removeMetalFromLump(newStack, element, (map.get(element)-amt));
				}
			}

			newStack.stackSize = newStackSize;
			event.craftMatrix.setInventorySlotContents(slot, newStack);

			return true;
		}
		return false;
	}
	private boolean metalHalving(ItemCraftedEvent event) {
		if (event.crafting.getItem() == ItemRegistry.metalLump){
			int amountMetal = 0;
			int slot = -1;

			for (int x = 0; x < event.craftMatrix.getSizeInventory(); x++) {
				if (event.craftMatrix.getStackInSlot(x) != null) {
					if (event.craftMatrix.getStackInSlot(x).getItem() == ItemRegistry.metalLump) {
						amountMetal++;
						slot = x;
						if (amountMetal > 1) {
							return false;
						}
					} else {
						return false;
					}
				}
			}

			if (amountMetal == 1) {
				ItemStack newStack = ItemRegistry.metalLump.getHalf(event.craftMatrix.getStackInSlot(slot));
				newStack.stackSize = 2;
				event.craftMatrix.setInventorySlotContents(slot, newStack);

				return true;
			}
		}
		return false;
	}

	/*
	private int getSlotFromItem(ItemStack item, IInventory craftMatrix, Item endItem){
		for(int i=0; i < craftMatrix.getSizeInventory(); i++) //Checks all the slots
		{               
			if(craftMatrix.getStackInSlot(i) != null) //If there is an item
			{
				ItemStack j = craftMatrix.getStackInSlot(i); //Gets the item
				if(j != null && j.getItem() == endItem) //If it's a knife
				{
					return i;
				}
			}
		}
		return 0;	
	}
	 */
}
