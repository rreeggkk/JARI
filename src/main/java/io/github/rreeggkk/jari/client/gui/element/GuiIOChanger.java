package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.IOGui;
import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.common.entity.tile.IIOControllable;

public class GuiIOChanger extends GuiRectangle{

	private int side;
	
	public GuiIOChanger(int x, int y, int w, int h, JARIGuiScreen<?> g, int side) {
		super(x, y, w, h, g);
		this.side = side;
	}
	
	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		text = IOGui.getSideText(side) + " - " + IOGui.getModeText(((IIOControllable)gui.getContainer().getTileEntity()).getSideIO(side));
		super.drawBackgroundLayer(f1, f2, f3);
	}
	
	@Override
	public boolean onClick(int guiX, int guiY, int mouseButton) {
		gui.mc.playerController.sendEnchantPacket(gui.getContainer().windowId,
				side + 1);
		return true;
	}
}
