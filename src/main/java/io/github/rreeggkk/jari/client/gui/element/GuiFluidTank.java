package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.GuiJARI;
import net.minecraftforge.fluids.FluidStack;

public class GuiFluidTank extends GuiRectangle {
	
	protected FluidStack fluid;
	protected int maxAmount;

	public GuiFluidTank(int x, int y, int w, GuiJARI<?> g, FluidStack f, int max) {
		super(x, y, w, 73, g);
		fluid = f;
		maxAmount = max;
	}
	
	public FluidStack getFluid() {
		return fluid;
	}
	public void setFluid(FluidStack fluid) {
		this.fluid = fluid;
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

		gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y, 0, 0, 18, 73);
		
		if (fluid != null) {
			int waterSize = (int) ((float) fluid.amount * 71 / maxAmount);
			drawFluid(fluid,
					gui.getGuiLeft() + x+1, gui.getGuiTop() + y+1 + (71 - waterSize),
					16, waterSize,
					71);

			gui.mc.getTextureManager().bindTexture(elementTextures);
		}

		gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y, 18, 0, 18, 73);
	}
	
	@Override
	public void drawForegroundLayer(int x, int y) {
		if (fluid != null) {
			text = fluid.amount + "/" + maxAmount + " mB";
		} else {
			text = "0/" + maxAmount + " mB";
		}
		super.drawForegroundLayer(x, y);
	}
}
