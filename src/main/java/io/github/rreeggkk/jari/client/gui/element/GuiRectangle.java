package io.github.rreeggkk.jari.client.gui.element;

public class GuiRectangle {
	private int x;
	private int y;
	private int h;
	private int w;

	public GuiRectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
	}

	public boolean isPointIn(int mx, int my) {
		if (mx > x && my > y && mx < x + w && my < y + h) {
			return true;
		}
		return false;
	}
}
