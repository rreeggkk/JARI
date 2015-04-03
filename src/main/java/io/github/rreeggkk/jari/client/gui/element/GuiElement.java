package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.RenderUtils;
import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class GuiElement {
	public static final ResourceLocation elementTextures = new ResourceLocation(
			ModInformation.ID + ":textures/gui/guiElements.png");
	protected int x, y, w, h;
	protected JARIGuiScreen<?> gui;

	public GuiElement(JARIGuiScreen<?> g) {
		this(0, 0, 0, 0, g);
	}

	public GuiElement(int x, int y, int w, int h, JARIGuiScreen<?> g) {
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
		gui = g;
	}

	public void setH(int h) {
		this.h = h;
	}

	public void setW(int w) {
		this.w = w;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getH() {
		return h;
	}

	public int getW() {
		return w;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void drawBackgroundLayer(float f1, float f2, float f3) {
	}

	public void drawForegroundLayer(int x, int y) {
	}

	public boolean onClick(int guiX, int guiY, int mouseButton) {
		return false;
	}

	public boolean isPointIn(int mx, int my) {
		if (mx > x && my > y && mx < x + w && my < y + h) {
			return true;
		}
		return false;
	}

	public void drawFluid(FluidStack fluid, int x, int y, int width,
			int height, int maxCapacity) {
		if (fluid == null || fluid.getFluid() == null) {
			return;
		}
		IIcon icon = fluid.getFluid().getIcon(fluid);
		gui.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		RenderUtils.setGLColorFromInt(fluid.getFluid().getColor(fluid));
		int fullX = width / 16;
		int fullY = height / 16;
		int lastX = width - fullX * 16;
		int lastY = height - fullY * 16;
		int level = fluid.amount * height / maxCapacity;
		int fullLvl = (height - level) / 16;
		int lastLvl = height - level - fullLvl * 16;
		for (int i = 0; i < fullX; i++) {
			for (int j = 0; j < fullY; j++) {
				if (j >= fullLvl) {
					drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16,
							j == fullLvl ? lastLvl : 0);
				}
			}
		}
		for (int i = 0; i < fullX; i++) {
			drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY,
					fullLvl == fullY ? lastLvl : 0);
		}
		for (int i = 0; i < fullY; i++) {
			if (i >= fullLvl) {
				drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16,
						i == fullLvl ? lastLvl : 0);
			}
		}
		drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY,
				fullLvl == fullY ? lastLvl : 0);
	}

	public void drawIcon(IIcon icon, int x, int y, int width, int height) {
		if (icon == null) {
			return;
		}
		gui.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y, gui.getZLevel(), icon.getMinU(),
				icon.getMinV());
		tess.addVertexWithUV(x, y + height, gui.getZLevel(), icon.getMinU(),
				icon.getMaxV());
		tess.addVertexWithUV(x + width, y + height, gui.getZLevel(),
				icon.getMaxU(), icon.getMaxV());
		tess.addVertexWithUV(x + width, y, gui.getZLevel(), icon.getMaxU(),
				icon.getMinV());
		tess.draw();
	}

	// The magic is here
	private void drawCutIcon(IIcon icon, int x, int y, int width, int height,
			int cut) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y + height, gui.getZLevel(), icon.getMinU(),
				icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + height, gui.getZLevel(),
				icon.getInterpolatedU(width), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + cut, gui.getZLevel(),
				icon.getInterpolatedU(width), icon.getInterpolatedV(cut));
		tess.addVertexWithUV(x, y + cut, gui.getZLevel(), icon.getMinU(),
				icon.getInterpolatedV(cut));
		tess.draw();
	}
}
