package io.github.rreeggkk.jari.client.gui.tab;

import io.github.rreeggkk.jari.client.gui.container.JARIGuiScreen;
import io.github.rreeggkk.jari.client.gui.element.GuiRectangle;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class GuiTab extends GuiRectangle {

	private ItemStack item;
	private RenderItem renderer;

	public GuiTab(int x, int y, int w, int h, String s, JARIGuiScreen<?> g,
			ItemStack item) {
		super(x, y, w, h, s, g);
		renderer = new RenderItem();
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	@Override
	public void drawBackgroundLayer(float f1, float f2, float f3) {
		super.drawBackgroundLayer(f1, f2, f3);

		gui.mc.getTextureManager().bindTexture(elementTextures);

		gui.drawTexturedModalRect(gui.getGuiLeft() + x, gui.getGuiTop() + y,
				36, 0, w, h);

		if (item != null) {
			renderer.renderItemIntoGUI(gui.mc.fontRenderer,
					gui.mc.renderEngine, item, gui.getGuiLeft() + x,
					gui.getGuiTop() + y, true);
		}

		// drawIcon(getIcon(), gui.getGuiLeft() + x + 1, gui.getGuiTop() + y +
		// 1, 16, 16);
	}
}
