package io.github.rreeggkk.jari.common.item;

import io.github.rreeggkk.jari.common.elements.ElementRegistry;
import io.github.rreeggkk.jari.common.util.ConfigHandler;
import io.github.rreeggkk.jari.common.util.MapUtil;
import io.github.rreeggkk.jari.common.util.TextHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

import org.apfloat.Apfloat;

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
			Map<String, Apfloat> metalMap = getContents(stack);
			Apfloat totalWeight = getTotalMass(stack);
			
			metalMap = MapUtil.sortByValue(metalMap, false);
			
			Apfloat hundred = new Apfloat(100);

			for (String metalName : metalMap.keySet()) {
				Apfloat percent = metalMap.get(metalName).divide(totalWeight).multiply(hundred);
				information.add(metalName
						+ ": "
						+ getWeightString(metalMap.get(metalName))
						+ " "
						+ String.format("%.2f", percent.doubleValue()) + "%");
			}

			information.add("Total weight: " + getWeightString(totalWeight));
		}
	}

	public void addMetalToLump(ItemStack stack, String metalName, Apfloat apfloat) {
		apfloat = apfloat.precision(128);
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
					metal.setString("Quantity", new Apfloat(metal.getString("Quantity")).precision(128).add(apfloat).toString());
					return;
				}
			}

			//If the metal is not found
			NBTTagCompound cpd = new NBTTagCompound();
			cpd.setString("Name", metalName);
			cpd.setString("Quantity", apfloat.toString());

			metals.appendTag(cpd);
			compound.setTag("Metals", metals);
		}
	}

	public void removeMetalFromLump(ItemStack stack, String metalName, Apfloat apfloat) {
		apfloat = apfloat.precision(128);
		if (stack.getItem() == this) {
			NBTTagCompound cpd = stack.getTagCompound();

			NBTTagList metals = cpd.getTagList("Metals", NBT.TAG_COMPOUND);

			for (int i = 0; i < metals.tagCount(); i++) {
				NBTTagCompound metal = metals.getCompoundTagAt(i);
				if (metal.getString("Name").equalsIgnoreCase(metalName)) {
					Apfloat amt = new Apfloat(metal.getString("Quantity"));
					amt = amt.precision(128).subtract(apfloat);
					if (amt.compareTo(new Apfloat(0)) <= 0) {
						metals.removeTag(i);
					} else {
						metal.setString("Quantity", amt.toString());
					}
					return;
				}
			}
		}
	}

	public HashMap<String, Apfloat> getContents(ItemStack stack) {
		if (stack.getItem() != this) return null;

		HashMap<String, Apfloat> metalMap = new HashMap<String, Apfloat>();

		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null) {
			return null;
		}

		NBTTagList metals = compound.getTagList("Metals", NBT.TAG_COMPOUND);

		for (int i = metals.tagCount() - 1; i >= 0; i--) {
			NBTTagCompound metal = metals.getCompoundTagAt(i);

			String name = metal.getString("Name");
			Apfloat amount = new Apfloat(metal.getString("Quantity")).precision(128);

			if (amount.compareTo(ConfigHandler.MIN_CUTOFF)<0) {
				metals.removeTag(i);
			} else {
				metalMap.put(name, amount);
			}
		}

		return metalMap;
	}

	private String getWeightString(Apfloat apfloat) {
		
		boolean kg = false;

		if (apfloat.compareTo(new Apfloat(1000)) > 0) {
			apfloat = apfloat.divide(new Apfloat(1000));
			kg = true;
		}
		
		String str = apfloat.precision(14).toString(true);

		return (str.length()<=15 ? str : apfloat.precision(14).toString(false)) + (kg ? "kg" : "g");
		
		//return apfloat.toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		// list.clear();
	}

	public Apfloat getTotalMass(ItemStack stack) {
		HashMap<String, Apfloat> contents = getContents(stack);
		if (contents != null) {
			Apfloat totalMass = new Apfloat(0).precision(128);
			for (Apfloat d : contents.values()) {
				totalMass = totalMass.add(d);
			}
			return totalMass;
		}
		return new Apfloat(0);
	}

	public ItemStack getHalf(ItemStack stack) {
		if (stack != null && stack.getItem() == this) {

			ItemStack newstack = new ItemStack(ItemRegistry.metalLump);
			HashMap<String, Apfloat> elements = ItemRegistry.metalLump.getContents(stack);
			for (String str : elements.keySet()) {
				ItemRegistry.metalLump.addMetalToLump(newstack, str, elements.get(str).divide(new Apfloat(2)));
			}

			return newstack;
		}
		return null;
	}
}
