package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiEnergyMeter;
import io.github.rreeggkk.jari.client.gui.element.GuiFluidTank;
import io.github.rreeggkk.jari.client.gui.element.GuiRedstoneControl;
import io.github.rreeggkk.jari.client.gui.element.GuiSideTab;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.discovery.ContainerType;

public class GuiHydraulicSeparator extends GuiJARI<ContainerHydraulicSeparator> {
	private static final ResourceLocation guiTextures = new ResourceLocation(
			ModInformation.ID
			+ ":textures/gui/container/hydraulicSeparator.png");

	private GuiEnergyMeter energy;
	private GuiFluidTank fluid;
	private GuiRedstoneControl redstoneControl;

	public GuiHydraulicSeparator(ContainerHydraulicSeparator container) {
		super(container);

		elements.add(energy = new GuiEnergyMeter(151, 6, 18, this, container.tile.getEnergy(), container.tile.getMaxEnergy()));
		elements.add(fluid = new GuiFluidTank(6, 6, 18, this, container.tile.getTank().getFluid(), TileEntityHydraulicSeparator.maxWater));
		elements.add(redstoneControl = new GuiRedstoneControl(xSize, 5, 18, 18, "jari.gui.element.redstoneControl", this, container.tile, 0));
	
		redstoneControl.setTextDirection(true);
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
		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f1, int f2, int f3) {	
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		energy.setEnergy(container.tile.getEnergy());
		fluid.setFluid(container.tile.getTank().getFluid());

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

		super.drawGuiContainerBackgroundLayer(f1, f2, f3);
	}
}
