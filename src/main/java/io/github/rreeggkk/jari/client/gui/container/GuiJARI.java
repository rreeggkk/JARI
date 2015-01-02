package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiElement;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

public class GuiJARI<T extends Container> extends GuiContainer {
	protected T container;
	protected int guiLeft;
	protected int guiTop;
	protected ArrayList<GuiElement> elements;

	public GuiJARI(T container) {
		super(container);
		this.container = container;
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
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		
		for (GuiElement e : elements) {
			e.drawBackgroundLayer(f1, f2, f3);
		}
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
