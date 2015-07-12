package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.common.entity.tile.ITextControllable;
import net.minecraft.client.gui.GuiScreen;

public class GuiNumberChangerButton extends GuiRectangle {

	private ITextControllable control;
	private int[] indicies;
	//private int[] values;

	public GuiNumberChangerButton(int x, int y, int w, int h, String s,
			JARIGuiScreen<?> g, ITextControllable controller, int[] indicies, int[] values) {
		super(x, y, w, h, s, g);
		this.indicies = indicies;
		control = controller;
		//this.values = values;
	}

	@Override
	public boolean onClick(int guiX, int guiY, int mouseButton) {
		if (mouseButton == 0) { //Right Mouse
			if (GuiScreen.isShiftKeyDown()) {
				gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
						indicies[5]);
			} else if (GuiScreen.isCtrlKeyDown()) {
				gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
						indicies[3]);
			} else {
				gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
						indicies[4]);
			}
		} else if (mouseButton == 1) { //Left Mouse
			if (GuiScreen.isShiftKeyDown()) {
				gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
						indicies[0]);
			} else if (GuiScreen.isCtrlKeyDown()) {
				gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
						indicies[2]);
			} else {
				gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
						indicies[1]);
			}
		}
		return false;
	}
	
	@Override
	public void drawForegroundLayer(int x, int y) {
	}

	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		setText(control.getString(indicies[0]));
		gui.getFontRendererObj().drawString(
				getText(),
				gui.getGuiLeft() + getX() + getW()/2 - gui.getFontRendererObj().getStringWidth(getText())/2,
				gui.getGuiTop() + getY() + getH()/2 - gui.getFontRendererObj().FONT_HEIGHT/2,
				4210752, false);
		super.drawBackgroundLayer(f1, f2, f3);
	}
}
