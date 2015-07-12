package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.common.entity.tile.ITextControllable;

public class GuiButton extends GuiRectangle {

	private int actionID, actionID2;
	private ITextControllable control;

	public GuiButton(int x, int y, int w, int h, String s,
			JARIGuiScreen<?> g, ITextControllable controller, int actionID, int actionID2) {
		super(x, y, w, h, s, g);
		this.actionID = actionID;
		this.actionID2 = actionID2;
		control = controller;
	}

	@Override
	public boolean onClick(int guiX, int guiY, int mouseButton) {
		gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
				(mouseButton == 1 ? actionID : actionID2));
		return false;
	}
	
	@Override
	public void drawForegroundLayer(int x, int y) {
		//super.drawForegroundLayer(x, y);
	}

	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		setText(control.getString(actionID));
		gui.getFontRendererObj().drawString(
				getText(),
				gui.getGuiLeft() + getX() + getW()/2 - gui.getFontRendererObj().getStringWidth(getText())/2,
				gui.getGuiTop() + getY() + getH()/2 - gui.getFontRendererObj().FONT_HEIGHT/2,
				4210752, false);
		super.drawBackgroundLayer(f1, f2, f3);
	}
}
