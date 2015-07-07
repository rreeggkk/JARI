package io.github.rreeggkk.jari.common.item;

import io.github.rreeggkk.jari.common.elements.ElementRegistry;
import io.github.rreeggkk.jari.common.util.TextHelper;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class ItemLumpOfMetal extends ItemBase {

	public ItemLumpOfMetal() {
		super("metal");
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player,
			List information, boolean bool) {
		if (!GuiScreen.isShiftKeyDown()) {
			information.add("Hold " + TextHelper.YELLOW + "SHIFT"
					+ TextHelper.LIGHT_GRAY + " to see more data");
		} else {
			HashMap<String, Double> metalMap = new HashMap<String, Double>();
			double totalWeight = 0;

			NBTTagCompound compound = stack.getTagCompound();
			if (compound == null) {
				return;
			}
			NBTTagList metals = compound.getTagList("Metals", NBT.TAG_COMPOUND);

			for (int i = 0; i < metals.tagCount(); i++) {
				NBTTagCompound metal = metals.getCompoundTagAt(i);

				String name = metal.getString("Name");
				double amount = metal.getDouble("Quantity");
				totalWeight += amount;

				metalMap.put(name, amount);
			}

			for (String metalName : metalMap.keySet()) {
				information.add(metalName
						+ ": "
						+ getWeightString(metalMap.get(metalName))
						+ " "
						+ String.format("%.2f", metalMap.get(metalName)
								/ totalWeight * 100) + "%");
			}

			information.add("Total weight: " + getWeightString(totalWeight));
		}
	}

	public void addMetalToLump(ItemStack stack, String metalName, double amount) {
		if (ElementRegistry.getProviderForElement(metalName) == null) {
			return;
		}
		if (stack.getItem() == this) {
			NBTTagCompound compound = stack.getTagCompound();
			if (compound == null) {
				stack.setTagCompound(new NBTTagCompound());
				compound = stack.getTagCompound();
			}

			NBTTagList metals = compound.getTagList("Metals", NBT.TAG_COMPOUND);

			for (int i = 0; i < metals.tagCount(); i++) {
				NBTTagCompound metal = metals.getCompoundTagAt(i);
				if (metal.getString("Name").equalsIgnoreCase(metalName)) {
					metal.setDouble("Quantity", metal.getDouble("Quantity")
							+ amount);
					return;
				}
			}

			//If the metal is not found
			NBTTagCompound cpd = new NBTTagCompound();
			cpd.setString("Name", metalName);
			cpd.setDouble("Quantity", amount);

			metals.appendTag(cpd);
			compound.setTag("Metals", metals);
		}
	}
	
	public void removeMetalFromLump(ItemStack stack, String metalName, double amount) {
		if (stack.getItem() == this) {
			NBTTagCompound cpd = stack.getTagCompound();
			
			NBTTagList metals = cpd.getTagList("Metals", NBT.TAG_COMPOUND);
			
			for (int i = 0; i < metals.tagCount(); i++) {
				NBTTagCompound metal = metals.getCompoundTagAt(i);
				if (metal.getString("Name").equalsIgnoreCase(metalName)) {
					double amt = metal.getDouble("Quantity");
					amt -= amount;
					if (amt <= 0) {
						metals.removeTag(i);
					} else {
						metal.setDouble("Quantity", amt);
					}
					return;
				}
			}
		}
	}
	
	public HashMap<String, Double> getContents(ItemStack stack) {
		if (stack.getItem() != this) return null;
		
		HashMap<String, Double> metalMap = new HashMap<String, Double>();
		
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null) {
			return null;
		}
		
		NBTTagList metals = compound.getTagList("Metals", NBT.TAG_COMPOUND);

		for (int i = 0; i < metals.tagCount(); i++) {
			NBTTagCompound metal = metals.getCompoundTagAt(i);

			String name = metal.getString("Name");
			double amount = metal.getDouble("Quantity");

			metalMap.put(name, amount);
		}
		
		return metalMap;
	}

	private String getWeightString(double amount) {
		boolean kg = false;

		if (amount > 1000) {
			amount /= 1000;
			kg = true;
		}

		return amount + (kg ? "kg" : "g");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		// list.clear();
	}

	public double getTotalMass(ItemStack stack) {
		HashMap<String, Double> contents = getContents(stack);
		if (contents != null) {
			double totalMass = 0;
			for (Double d : contents.values()) {
				totalMass += d;
			}
			return totalMass;
		}
		return 0;
	}

	public ItemStack getHalf(ItemStack stack) {
		if (stack != null && stack.getItem() == this) {
			
			stack = stack.copy();
	 		HashMap<String, Double> elements = ItemRegistry.metalLump.getContents(stack);
	 		for (String str : elements.keySet()) {
	 	 		ItemRegistry.metalLump.removeMetalFromLump(stack, str, elements.get(str)/2.0);
	 		}
	 		
	 		return stack;
		}
		return null;
	}
}
