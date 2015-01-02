package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiEnergyMeter;
import io.github.rreeggkk.jari.client.gui.element.GuiFluidTank;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiHydraulicSeparator extends GuiJARI<ContainerHydraulicSeparator> {
	private static final ResourceLocation guiTextures = new ResourceLocation(
			ModInformation.ID
			+ ":textures/gui/container/hydraulicSeparator.png");
	
	private GuiEnergyMeter energy;
	private GuiFluidTank fluid;

	public GuiHydraulicSeparator(ContainerHydraulicSeparator container) {
		super(container);

		elements.add(energy = new GuiEnergyMeter(151, 6, 18, this, container.tile.getEnergy(), container.tile.getMaxEnergy()));
		elements.add(fluid = new GuiFluidTank(6, 6, 18, this, container.tile.getTank().getFluid(), TileEntityHydraulicSeparator.maxWater));
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		String s = container.tile.hasCustomInventoryName() ? container.tile
				.getInventoryName() : I18n.format(
						container.tile.getInventoryName(), new Object[0]);
				fontRendererObj.drawString(s,
						xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
				fontRendererObj.drawString(
						I18n.format("container.inventory", new Object[0]), 28,
						ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f1, int f2, int f3) {	
		energy.setEnergy(container.tile.getEnergy());
		fluid.setFluid(container.tile.getTank().getFluid());
		
		super.drawGuiContainerBackgroundLayer(f1, f2, f3);
		
		mc.getTextureManager().bindTexture(guiTextures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if (container.tile.isRunning()) {
			int flameSize = container.tile.getFlameScaled(13);
			drawTexturedModalRect(guiLeft + 81, guiTop + 63 + 13 - flameSize,
					176, 13 - flameSize, 14, flameSize);
			// drawTexturedModalRect(guiLeft + 81, guiTop + 63, 176, 0, 14, 13);
			drawTexturedModalRect(guiLeft + 77, guiTop + 36, 176, 14,
					24 - container.tile.getProgressScaled(24), 17);
		}
	}
}
