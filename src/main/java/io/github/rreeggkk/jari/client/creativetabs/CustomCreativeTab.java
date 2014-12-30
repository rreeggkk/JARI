package io.github.rreeggkk.jari.client.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CustomCreativeTab extends CreativeTabs {
	
	Item icon;
	
	public CustomCreativeTab(String name) {
		super(name);
	}

	public CustomCreativeTab(String name, Item icon) {
		super(name);
		this.icon = icon;
	}
	
	public void setIcon(Item icon) {
		this.icon = icon;
	}

	@Override
	public Item getTabIconItem() {
		return icon;
	}
}
