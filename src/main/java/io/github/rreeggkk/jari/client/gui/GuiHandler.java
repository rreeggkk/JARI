package io.github.rreeggkk.jari.client.gui;

import io.github.rreeggkk.jari.client.gui.container.GuiHydraulicSeparator;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.jari.common.reference.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null) {
			if (ID == GuiIDs.HYDRAULIC_SEPARATOR) {
				return new ContainerHydraulicSeparator(player.inventory,
						(TileEntityHydraulicSeparator) tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null) {
			if (ID == GuiIDs.HYDRAULIC_SEPARATOR) {
				return new GuiHydraulicSeparator(
						new ContainerHydraulicSeparator(player.inventory,
								(TileEntityHydraulicSeparator) tileEntity));
			}
		}
		return null;
	}
}
