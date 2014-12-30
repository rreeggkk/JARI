package io.github.rreeggkk.jari.client.gui.container;

import io.github.rreeggkk.jari.client.gui.element.GuiRectangle;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.jari.common.reference.ModInformation;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public class GuiHydraulicSeparator extends GuiContainer  {
    private static final ResourceLocation guiTextures = new ResourceLocation(ModInformation.ID + ":textures/gui/container/hydraulicSeparator.png");
	private ContainerHydraulicSeparator container;
	public int guiLeft;
	public int guiTop;
	private GuiRectangle energyBox, waterBox;

	public GuiHydraulicSeparator(ContainerHydraulicSeparator container) {
		super(container);
		this.container = container;

		energyBox = new GuiRectangle(165, 7, 4, 72);
		waterBox = new GuiRectangle(6, 6, 18, 73);
	}

	   /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String s = this.container.tile.hasCustomInventoryName() ? this.container.tile.getInventoryName() : I18n.format(this.container.tile.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 28, this.ySize - 96 + 2, 4210752);
        
        if (energyBox.isPointIn(x - guiLeft, y - guiTop)) {
        	ArrayList<String> list = new ArrayList<String>();
        	list.add(container.tile.getEnergy() + "/" + container.tile.getMaxEnergy() + " RF");
        	drawHoveringText(list, x - guiLeft, y - guiTop, fontRendererObj);
        }
        if (waterBox.isPointIn(x - guiLeft, y - guiTop)) {
        	ArrayList<String> list = new ArrayList<String>();
        	list.add(container.tile.getWaterCount() + "/" + TileEntityHydraulicSeparator.maxWater + " mB");
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
        
        //h = 6
        //k = 6
        int waterSize = container.tile.getEnergyScaled(71);
        drawFluid(this.container.tile.getTank().getFluid(), 1, guiLeft + 7, guiTop + 7 + waterSize - 71, 16, waterSize);
        
        drawTexturedModalRect(guiLeft + 6, guiTop + 6, 176, 103, 18, 73);
        
        //w = 18
        //h = 73
        //h = 176
        //k = 103
    }
    
	private void drawFluid(FluidStack fluid, int level, int x, int y, int width, int height){
		if(fluid == null || fluid.getFluid() == null) {
			return;
		}
		IIcon icon = fluid.getFluid().getIcon(fluid);
		//mc.renderEngine.bindTexture(fluid.getFluid().getBlock().);
		int fullX = width / 16;
		int fullY = height / 16;
		int lastX = width - fullX * 16;
		int lastY = height - fullY * 16;
		int fullLvl = (height - level) / 16;
		int lastLvl = (height - level) - fullLvl * 16;
		for(int i = 0; i < fullX; i++) {
			for(int j = 0; j < fullY; j++) {
				if(j >= fullLvl) {
					drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16, j == fullLvl ? lastLvl : 0);
				}
			}
		}
		for(int i = 0; i < fullX; i++) {
			drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY, fullLvl == fullY ? lastLvl : 0);
		}
		for(int i = 0; i < fullY; i++) {
			if(i >= fullLvl) {
				drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16, i == fullLvl ? lastLvl : 0);
			}
		}
		drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY, fullLvl == fullY ? lastLvl : 0);
	}
	//The magic is here
	private void drawCutIcon(IIcon icon, int x, int y, int width, int height, int cut){
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y + height, zLevel, icon.getMinU(), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + height, zLevel, icon.getInterpolatedU(width), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + cut, zLevel, icon.getInterpolatedU(width), icon.getInterpolatedV(cut));
		tess.addVertexWithUV(x, y + cut, zLevel, icon.getMinU(), icon.getInterpolatedV(cut));
		tess.draw();
	}

}
