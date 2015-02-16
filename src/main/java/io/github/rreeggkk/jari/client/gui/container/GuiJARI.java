package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiElement;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiJARI<T extends Container> extends GuiContainer {
	protected T container;
	protected int guiLeft;
	protected int guiTop;
	protected ArrayList<GuiElement> elements;

	public GuiJARI(T container) {
		super(container);
		this.container = container;
		elements = new ArrayList<GuiElement>();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		for (GuiElement e : elements) {
			e.drawForegroundLayer(x, y);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f1, int f2, int f3) {
		
		for (GuiElement e : elements) {
			e.drawBackgroundLayer(f1, f2, f3);
		}
	}
	
	@Override
	protected void mouseClicked(int guiX, int guiY, int mouseButton) {
		for (GuiElement e : elements) {
			if (e.isPointIn(guiX - guiLeft, guiY - guiTop) && e.onClick(guiX - guiLeft, guiY - guiTop, mouseButton))
				break;
		}
		
		super.mouseClicked(guiX, guiY, mouseButton);
		//0 = left, 1 = right, 2 = middle
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
}
