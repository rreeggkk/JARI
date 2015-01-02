package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.GuiJARI;

public class GuiEnergyMeter extends GuiRectangle {
	
	protected int energy;
	protected int maxAmount;

	public GuiEnergyMeter(int x, int y, int w, GuiJARI<?> g, int e, int max) {
		super(x, y, w, 73, g);
		energy = e;
		maxAmount = max;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	public int getMaxAmount() {
		return maxAmount;
	}
	
	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		super.drawBackgroundLayer(f1, f2, f3);
		
		gui.mc.getTextureManager().bindTexture(elementTextures);

		gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y, 54, 0, 18, 73);
		
		int energySize = (int) ((float) energy * 71 / maxAmount);
		gui.drawTexturedModalRect(
				gui.getGuiLeft() + x+1, gui.getGuiTop() + y+1 + (71 - energySize),
				72, 72 - energySize,
				16, energySize);

	}
	
	@Override
	public void drawForegroundLayer(int x, int y) {
		text = energy + "/" + maxAmount + " RF";
		super.drawForegroundLayer(x, y);
	}
}
