package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiEnergyMeter;
import io.github.rreeggkk.jari.common.inventory.ContainerRTG;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiRTG extends
		JARIGuiScreen<ContainerRTG> {
	private static final ResourceLocation mainGUITexture = new ResourceLocation(
			ModInformation.ID
					+ ":textures/gui/container/rtg.png");

	// private GuiEnergyMeter energy;
	// private GuiFluidTank fluid;
	// private GuiTabRedstoneControl redstoneControl;

	public GuiRTG(ContainerRTG container) {
		super(container);

		{
			GuiJARI<ContainerRTG> mainScreen = new MainGui(this);
			mainScreen.elements.add(new GuiEnergyMeter(151, 6, 18, this,
					container.tile));
			/*
			mainScreen.elements
					.add(new GuiTabRedstoneControl(xSize, 5, 18, 18,
							"jari.gui.element.redstoneControl", this,
							container.tile, 0).setTextDirection(true));
			*/
			screens.add(mainScreen);
			currentScreen = mainScreen;
		}
	}

	public class MainGui extends GuiJARI<ContainerRTG> {
		public MainGui(JARIGuiScreen<ContainerRTG> mainScreen) {
			super(mainScreen);
		}

		@Override
		protected void drawForeground(int x, int y) {
			String s = container.tile.hasCustomInventoryName() ? container.tile
					.getInventoryName() : I18n.format(
					container.tile.getInventoryName(), new Object[0]);
			fontRendererObj.drawString(s,
					xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6,
					4210752);
			
			String rft = container.tile.getInfoEnergyPerTick() + " RF/t";
			fontRendererObj.drawString(rft,
					xSize / 2 - fontRendererObj.getStringWidth(rft) / 2, 15,
					4210752);
			
			
			fontRendererObj.drawString(
					I18n.format("container.inventory", new Object[0]), 8,
					ySize - 96 + 2, 4210752);
			super.drawForeground(x, y);
		}

		@Override
		protected void drawBackground(float f1, int f2, int f3) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			guiLeft = (width - xSize) / 2;
			guiTop = (height - ySize) / 2;

			mc.getTextureManager().bindTexture(mainGUITexture);
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
			
			super.drawBackground(f1, f2, f3);
		}
	}
}
