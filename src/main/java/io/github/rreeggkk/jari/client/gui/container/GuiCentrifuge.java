package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiButton;
import io.github.rreeggkk.jari.client.gui.element.GuiEnergyMeter;
import io.github.rreeggkk.jari.client.gui.element.GuiNumberChangerButton;
import io.github.rreeggkk.jari.client.gui.tab.GuiTabRedstoneControl;
import io.github.rreeggkk.jari.common.inventory.ContainerCentrifuge;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiCentrifuge extends
		JARIGuiScreen<ContainerCentrifuge> {
	private static final ResourceLocation mainGUITexture = new ResourceLocation(
			ModInformation.ID
					+ ":textures/gui/container/centrifuge.png");

	// private GuiEnergyMeter energy;
	// private GuiFluidTank fluid;
	// private GuiTabRedstoneControl redstoneControl;

	public GuiCentrifuge(ContainerCentrifuge container) {
		super(container);

		{
			GuiJARI<ContainerCentrifuge> mainScreen = new MainGui(this);
			mainScreen.elements.add(new GuiEnergyMeter(151, 6, 18, this,
					container.tile));
			mainScreen.elements
					.add(new GuiTabRedstoneControl(xSize, 5, 18, 18,
							"jari.gui.element.redstoneControl", this,
							container.tile, 0).setTextDirection(true));
			mainScreen.elements
				.add(new GuiButton(3, 54, 72, 18,
						"jari.gui.element.elementType", this,
						container.tile, 1, 8));
			mainScreen.elements
				.add(new GuiNumberChangerButton(5, 5, 35, 18,
						"jari.gui.element.percent", this,
						container.tile, new int[]{2,3,4,5,6,7}, new int[]{-10, -5, -1, 1, 5, 10}));
			screens.add(mainScreen);
			currentScreen = mainScreen;
		}
	}

	public class MainGui extends GuiJARI<ContainerCentrifuge> {
		public MainGui(JARIGuiScreen<ContainerCentrifuge> mainScreen) {
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
			fontRendererObj.drawString(
					I18n.format("container.inventory", new Object[0]), 28,
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

			if (container.tile.isRunning()) {
				int flameSize = container.tile.getFlameScaled(13);
				drawTexturedModalRect(guiLeft + 81, guiTop + 63 + 13
						- flameSize, 176, 13 - flameSize, 14, flameSize);
				// drawTexturedModalRect(guiLeft + 81, guiTop + 63, 176, 0, 14,
				// 13);
				drawTexturedModalRect(guiLeft + 77, guiTop + 36, 176, 14,
						24 - container.tile.getProgressScaled(24), 17);
			}
			super.drawBackground(f1, f2, f3);
		}
	}
}
