package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import net.minecraftforge.fluids.FluidTank;

public class GuiFluidTank extends GuiRectangle {
	
	protected FluidTank fluid;

	public GuiFluidTank(int x, int y, int w, JARIGuiScreen<?> g, FluidTank f) {
		super(x, y, w, 73, g);
		fluid = f;
	}
	
	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		super.drawBackgroundLayer(f1, f2, f3);
		
		gui.mc.getTextureManager().bindTexture(elementTextures);

		gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y, 0, 0, 18, 73);
		
		if (fluid != null && fluid.getFluid() != null) {
			int waterSize = (int) ((float) fluid.getFluid().amount * 71 / fluid.getCapacity());
			drawFluid(fluid.getFluid(),
					gui.getGuiLeft() + x+1, gui.getGuiTop() + y+1 + (71 - waterSize),
					16, waterSize,
					71);

			gui.mc.getTextureManager().bindTexture(elementTextures);
		}

		gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y, 18, 0, 18, 73);
	}
	
	@Override
	public void drawForegroundLayer(int x, int y) {
		if (fluid != null && fluid.getFluid() != null) {
			text = fluid.getFluid().amount + "/" + fluid.getCapacity() + " mB";
		} else {
			text = "0/" + fluid.getCapacity() + " mB";
		}
		super.drawForegroundLayer(x, y);
	}
}
