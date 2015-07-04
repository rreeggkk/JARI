package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiIOChanger;
import io.github.rreeggkk.jari.client.gui.element.GuiRectangle;
import io.github.rreeggkk.jari.common.entity.tile.IIOControllable;
import io.github.rreeggkk.jari.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.jari.common.inventory.ITileContainer;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class IOGui<T extends Container & ITileContainer & IIOControllable> extends GuiJARI<T> {
	private static final ResourceLocation blankGUITexture = new ResourceLocation(
			ModInformation.ID
					+ ":textures/gui/container/blankGui.png");
	
	public IOGui(JARIGuiScreen<T> mainScreen) {
		super(mainScreen);
		
		elements.add(new GuiIOChanger(mainScreen.getXSize()/2, mainScreen.getYSize()/2, 16, 16, mainScreen, 0));
		elements.add(new GuiIOChanger(mainScreen.getXSize()/2, mainScreen.getYSize()/2 + 20, 16, 16, mainScreen, 1));
		elements.add(new GuiIOChanger(mainScreen.getXSize()/2 + 20, mainScreen.getYSize()/2 - 20, 16, 16, mainScreen, 2));
		elements.add(new GuiIOChanger(mainScreen.getXSize()/2, mainScreen.getYSize()/2 - 20, 16, 16, mainScreen, 3));
		elements.add(new GuiIOChanger(mainScreen.getXSize()/2 + 20, mainScreen.getYSize()/2, 16, 16, mainScreen, 4));
		elements.add(new GuiIOChanger(mainScreen.getXSize()/2 - 20, mainScreen.getYSize()/2, 16, 16, mainScreen, 5));
	}

	@Override
	protected void drawForeground(int x, int y) {
		super.drawForeground(x, y);
	}

	@Override
	protected void drawBackground(float f1, int f2, int f3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mainScreen.guiLeft = (mainScreen.width - mainScreen.getXSize()) / 2;
		mainScreen.guiTop = (mainScreen.height - mainScreen.getYSize()) / 2;

		mainScreen.mc.getTextureManager().bindTexture(blankGUITexture);
		mainScreen.drawTexturedModalRect(mainScreen.guiLeft, mainScreen.guiTop, 0, 0, mainScreen.getXSize(), mainScreen.getYSize());

		super.drawBackground(f1, f2, f3);
	}
	
	
	public static String getSideText(int side) {
		switch (side) {
			case 0:
				return "Front";
			case 1:
				return "Top";
			case 2:
				return "Back";
			case 3:
				return "Bottom";
			case 4:
				return "Right";
			case 5:
				return "Left";
		}
		return "N/A";
	}
	public static String getModeText(int mode) {
		switch (mode) {
			case 0:
				return "Closed";
			case 1:
				return "Input";
			case 2:
				return "Output";
		}
		return "N/A";
	}
}