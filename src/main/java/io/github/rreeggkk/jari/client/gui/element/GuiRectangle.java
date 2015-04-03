package io.github.rreeggkk.jari.client.gui.element;

import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.common.util.TextHelper;

import java.util.ArrayList;

public class GuiRectangle extends GuiElement {
	protected String text;
	protected boolean textDirection;

	public GuiRectangle(int x, int y, int w, int h, JARIGuiScreen<?> g) {
		super(x, y, w, h, g);
	}

	public GuiRectangle(int x, int y, int w, int h, String s, JARIGuiScreen<?> g) {
		super(x, y, w, h, g);
		text = s;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public GuiRectangle setTextDirection(boolean textDirection) {
		this.textDirection = textDirection;
		return this;
	}

	public boolean getTextDirection() {
		return textDirection;
	}

	@Override
	public void drawForegroundLayer(int x, int y) {
		if (text != null
				&& isPointIn(x - gui.getGuiLeft(), y - gui.getGuiTop())) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(TextHelper.localize(text));
			gui.drawHoveringText(
					list,
					x
							- gui.getGuiLeft()
							- (textDirection ? gui.mc.fontRenderer
									.getStringWidth(TextHelper.localize(text)) + 16
									: 0), y - gui.getGuiTop());
		}
	}
}
