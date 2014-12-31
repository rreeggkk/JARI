package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.RenderUtils;
import io.github.rreeggkk.jari.client.gui.element.GuiRectangle;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.jari.common.reference.ModInformation;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public class GuiHydraulicSeparator extends GuiContainer {
	private static final ResourceLocation guiTextures = new ResourceLocation(
			ModInformation.ID
			+ ":textures/gui/container/hydraulicSeparator.png");
	private ContainerHydraulicSeparator container;
	public int guiLeft;
	public int guiTop;
	private GuiRectangle energyBox, waterBox;

	public GuiHydraulicSeparator(ContainerHydraulicSeparator container) {
		super(container);
		this.container = container;

		energyBox = new GuiRectangle(165, 7, 4, 72);
		waterBox = new GuiRectangle(6, 6, 18, 73);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String s = container.tile.hasCustomInventoryName() ? container.tile
				.getInventoryName() : I18n.format(
						container.tile.getInventoryName(), new Object[0]);
				fontRendererObj.drawString(s,
						xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
				fontRendererObj.drawString(
						I18n.format("container.inventory", new Object[0]), 28,
						ySize - 96 + 2, 4210752);

				if (energyBox.isPointIn(x - guiLeft, y - guiTop)) {
					ArrayList<String> list = new ArrayList<String>();
					list.add(container.tile.getEnergy() + "/"
							+ container.tile.getMaxEnergy() + " RF");
					drawHoveringText(list, x - guiLeft, y - guiTop, fontRendererObj);
				}
				if (waterBox.isPointIn(x - guiLeft, y - guiTop)) {
					ArrayList<String> list = new ArrayList<String>();
					list.add(container.tile.getWaterCount() + "/"
							+ TileEntityHydraulicSeparator.maxWater + " mB");
					drawHoveringText(list, x - guiLeft, y - guiTop, fontRendererObj);
				}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTextures);
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int energySize = container.tile.getEnergyScaled(72);
		drawTexturedModalRect(guiLeft + 165, guiTop + 7 + 72 - energySize, 176,
				31 + 72 - energySize, 4, energySize);

		if (container.tile.isRunning()) {
			int flameSize = container.tile.getFlameScaled(13);
			drawTexturedModalRect(guiLeft + 81, guiTop + 63 + 13 - flameSize,
					176, 13 - flameSize, 14, flameSize);
			// drawTexturedModalRect(guiLeft + 81, guiTop + 63, 176, 0, 14, 13);
			drawTexturedModalRect(guiLeft + 77, guiTop + 36, 176, 14,
					24 - container.tile.getProgressScaled(24), 17);
		}

		// h = 6
		// k = 6
		int waterSize = container.tile.getWaterScaled(71);
		drawFluid(container.tile.getTank().getFluid(),
				guiLeft + 7, guiTop + 7 + (71 - waterSize),
				16, waterSize,
				71);

		mc.getTextureManager().bindTexture(guiTextures);

		drawTexturedModalRect(guiLeft + 6, guiTop + 6, 176, 103, 18, 73);

		// w = 18
		// h = 73
		// h = 176
		// k = 103
	}

	public void drawFluid(FluidStack fluid, int x, int y, int width, int height, int maxCapacity) {
		if (fluid == null || fluid.getFluid() == null) {
			return;
		}
		IIcon icon = fluid.getFluid().getIcon(fluid);
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		RenderUtils.setGLColorFromInt(fluid.getFluid().getColor(fluid));
		int fullX = width / 16;
		int fullY = height / 16;
		int lastX = width - fullX * 16;
		int lastY = height - fullY * 16;
		int level = fluid.amount * height / maxCapacity;
		int fullLvl = (height - level) / 16;
		int lastLvl = (height - level) - fullLvl * 16;
		for (int i = 0; i < fullX; i++) {
			for (int j = 0; j < fullY; j++) {
				if (j >= fullLvl) {
					drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16, j == fullLvl ? lastLvl : 0);
				}
			}
		}
		for (int i = 0; i < fullX; i++) {
			drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY, fullLvl == fullY ? lastLvl : 0);
		}
		for (int i = 0; i < fullY; i++) {
			if (i >= fullLvl) {
				drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16, i == fullLvl ? lastLvl : 0);
			}
		}
		drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY, fullLvl == fullY ? lastLvl : 0);
	}

	//The magic is here
	private void drawCutIcon(IIcon icon, int x, int y, int width, int height, int cut) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y + height, zLevel, icon.getMinU(), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + height, zLevel, icon.getInterpolatedU(width), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + cut, zLevel, icon.getInterpolatedU(width), icon.getInterpolatedV(cut));
		tess.addVertexWithUV(x, y + cut, zLevel, icon.getMinU(), icon.getInterpolatedV(cut));
		tess.draw();
	}
}
