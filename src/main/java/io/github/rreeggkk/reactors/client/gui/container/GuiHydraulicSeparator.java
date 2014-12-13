package io.github.rreeggkk.reactors.client.gui.container;

import io.github.rreeggkk.reactors.client.gui.element.GuiRectangle;
import io.github.rreeggkk.reactors.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.reactors.common.reference.ModInformation;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiHydraulicSeparator extends GuiContainer  {
    private static final ResourceLocation guiTextures = new ResourceLocation(ModInformation.ID + ":textures/gui/container/hydraulicSeparator.png");
	private ContainerHydraulicSeparator container;
	public int guiLeft;
	public int guiTop;
	private GuiRectangle energyBox;

	public GuiHydraulicSeparator(ContainerHydraulicSeparator container) {
		super(container);
		this.container = container;

		energyBox = new GuiRectangle(165, 7, 4, 72);
	}

	   /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String s = this.container.tile.hasCustomInventoryName() ? this.container.tile.getInventoryName() : I18n.format(this.container.tile.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 12, this.ySize - 96 + 2, 4210752);
        
        if (energyBox.isPointIn(x - guiLeft, y - guiTop)) {
        	ArrayList<String> list = new ArrayList<String>();
        	list.add(container.tile.getEnergy() + "/" + container.tile.getMaxEnergy() + " RF");
        	drawHoveringText(list, x - guiLeft, y - guiTop, fontRendererObj);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

        int energySize = container.tile.getEnergyScaled(72);
        drawTexturedModalRect(guiLeft + 165,guiTop + 7 + 72 - energySize,
        		176, 31 + 72 - energySize,
        		4, energySize);
        
        if (container.tile.isRunning()){
        	int flameSize = container.tile.getFlameScaled(13);
            drawTexturedModalRect(guiLeft + 81,guiTop + 63 + 13 - flameSize,
            		176, 13 - flameSize,
            		14, flameSize);
        	//drawTexturedModalRect(guiLeft + 81, guiTop + 63, 176, 0, 14, 13);
        	drawTexturedModalRect(guiLeft + 77, guiTop + 36, 176, 14, 24 - container.tile.getProgressScaled(24), 17);
        }
    }

}
