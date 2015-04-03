package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.common.entity.tile.IEnergyAccessable;

public class GuiEnergyMeter extends GuiRectangle {

	protected IEnergyAccessable energyTile;

	public GuiEnergyMeter(int x, int y, int w, JARIGuiScreen<?> g,
			IEnergyAccessable energyTile) {
		super(x, y, w, 73, g);
		this.energyTile = energyTile;
	}

	public int getEnergy() {
		return energyTile.getEnergy();
	}

	public int getMaxAmount() {
		return energyTile.getMaxEnergy();
	}

	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		super.drawBackgroundLayer(f1, f2, f3);

		gui.mc.getTextureManager().bindTexture(elementTextures);

		gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y,
				54, 0, 18, 73);

		int energySize = (int) ((float) getEnergy() * 71 / getMaxAmount());
		gui.drawTexturedModalRect(gui.getGuiLeft() + x + 1, gui.getGuiTop() + y
				+ 1 + 71 - energySize, 72, 72 - energySize, 16, energySize);

	}

	@Override
	public void drawForegroundLayer(int x, int y) {
		text = getEnergy() + "/" + getMaxAmount() + " RF";
		super.drawForegroundLayer(x, y);
	}
}
