package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiElement;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiJARI<T extends Container>{
	protected ArrayList<GuiElement> elements;
	protected JARIGuiScreen<T> mainScreen;

	public GuiJARI(JARIGuiScreen<T> mainScreen) {
		elements = new ArrayList<GuiElement>();
		this.mainScreen = mainScreen;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawForeground(int x, int y) {
		for (GuiElement e : elements) {
			e.drawForegroundLayer(x, y);
		}
	}
	protected void drawBackground(float f1, int f2, int f3) {
		for (GuiElement e : elements) {
			e.drawBackgroundLayer(f1, f2, f3);
		}
	}
	
	protected void mouseClicked(int guiX, int guiY, int mouseButton) {
		for (GuiElement e : elements) {
			if (e.isPointIn(guiX - mainScreen.guiLeft, guiY - mainScreen.guiTop) && e.onClick(guiX - mainScreen.guiLeft, guiY - mainScreen.guiTop, mouseButton))
				break;
		}

		//0 = left, 1 = right, 2 = middle
	}
}
