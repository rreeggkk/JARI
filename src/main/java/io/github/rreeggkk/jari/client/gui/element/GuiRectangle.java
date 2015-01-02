package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.GuiJARI;

import java.util.ArrayList;

public class GuiRectangle extends GuiElement{
	protected String text;

	public GuiRectangle(int x, int y, int w, int h, GuiJARI<?> g) {
		super(x, y, w, h, g);
	}
	public GuiRectangle(int x, int y, int w, int h, String s, GuiJARI<?> g) {
		super(x, y, w, h, g);
		this.text = s;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	@Override
	public void drawForegroundLayer(int x, int y) {
		if (text != null && isPointIn(x - gui.getGuiLeft(), y - gui.getGuiTop())) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(text);
			gui.drawHoveringText(list, x - gui.getGuiLeft(), y - gui.getGuiTop());
		}
	}
}
