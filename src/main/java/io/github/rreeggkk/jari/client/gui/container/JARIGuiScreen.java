package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.common.inventory.ITileContainer;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class JARIGuiScreen<T extends Container & ITileContainer> extends GuiContainer {
	protected T container;
	protected int guiLeft;
	protected int guiTop;
	protected ArrayList<GuiJARI<T>> screens;
	protected GuiJARI<T> currentScreen;

	public JARIGuiScreen(T container) {
		super(container);
		this.container = container;
		screens = new ArrayList<GuiJARI<T>>();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		currentScreen.drawForeground(x, y);
		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f1, int f2, int f3) {
		currentScreen.drawBackground(f1, f2, f3);
	}

	@Override
	protected void mouseClicked(int guiX, int guiY, int mouseButton) {
		currentScreen.mouseClicked(guiX, guiY, mouseButton);
		super.mouseClicked(guiX, guiY, mouseButton);
	}

	public T getContainer() {
		return container;
	}

	public int getGuiLeft() {
		return guiLeft;
	}

	public int getGuiTop() {
		return guiTop;
	}

	public float getZLevel() {
		return zLevel;
	}

	public void drawHoveringText(ArrayList<String> list, int i, int j) {
		this.drawHoveringText(list, i, j, fontRendererObj);
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}
}
