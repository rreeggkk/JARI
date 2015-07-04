package io.github.rreeggkk.jari.client.gui.tab;

import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.common.entity.tile.IRedstoneControllable;
import io.github.rreeggkk.jari.common.enums.RedstonePowerMode;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiTabRedstoneControl extends GuiTab {

	private IRedstoneControllable controller;
	private int actionID;

	public GuiTabRedstoneControl(int x, int y, int w, int h, String s,
			JARIGuiScreen<?> g, IRedstoneControllable controller, int actionID) {
		super(x, y, w, h, s, g, getItemFromPowerMode(controller.getPowerMode()));
		this.controller = controller;
		this.actionID = actionID;
	}

	@Override
	public boolean onClick(int guiX, int guiY, int mouseButton) {
		gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
				actionID);
		return false;
	}

	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		setItem(getItemFromPowerMode(controller.getPowerMode()));
		setText(controller.getPowerMode().getDelocString());
		super.drawBackgroundLayer(f1, f2, f3);
	}

	private static ItemStack getItemFromPowerMode(RedstonePowerMode powerMode) {
		switch (powerMode) {
			case ALWAYS_OFF:
				return new ItemStack(Items.gunpowder);
			case ALWAYS_ON:
				return new ItemStack(Items.redstone);
			case REQUIRED_ON:
				return new ItemStack(Blocks.redstone_torch);
			case REQUIRED_OFF:
				return new ItemStack(Items.stick);
		}
		return null;
	}
}
