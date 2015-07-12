package io.github.rreeggkk.jari.common.gui;

import static io.github.rreeggkk.jari.common.reference.GuiIDs.CENTRIFUGE;
import static io.github.rreeggkk.jari.common.reference.GuiIDs.CHEMICAL_SEPARATOR;
import static io.github.rreeggkk.jari.common.reference.GuiIDs.HYDRAULIC_SEPARATOR;
import static io.github.rreeggkk.jari.common.reference.GuiIDs.RTG;
import io.github.rreeggkk.jari.client.gui.container.GuiCentrifuge;
import io.github.rreeggkk.jari.client.gui.container.GuiChemicalSeparator;
import io.github.rreeggkk.jari.client.gui.container.GuiHydraulicSeparator;
import io.github.rreeggkk.jari.client.gui.container.GuiRTG;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityCentrifuge;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityChemicalSeparator;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityHydraulicSeparator;
import io.github.rreeggkk.jari.common.entity.tile.TileEntityRTG;
import io.github.rreeggkk.jari.common.inventory.ContainerCentrifuge;
import io.github.rreeggkk.jari.common.inventory.ContainerChemicalSeparator;
import io.github.rreeggkk.jari.common.inventory.ContainerHydraulicSeparator;
import io.github.rreeggkk.jari.common.inventory.ContainerRTG;
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
			//Static import used previously (These constants belong to common.referece.GuiIDs)
			if (ID == HYDRAULIC_SEPARATOR) {
				return new ContainerHydraulicSeparator(player.inventory,
						(TileEntityHydraulicSeparator) tileEntity);
			} else if (ID == RTG) {
				return new ContainerRTG(player.inventory,
						(TileEntityRTG) tileEntity);
			} else if (ID == CHEMICAL_SEPARATOR) {
				return new ContainerChemicalSeparator(player.inventory,
						(TileEntityChemicalSeparator) tileEntity);
			} else if (ID == CENTRIFUGE) {
				return new ContainerCentrifuge(player.inventory,
						(TileEntityCentrifuge) tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null) {
			//Static import used previously (These constants belong to common.referece.GuiIDs)
			if (ID == HYDRAULIC_SEPARATOR) {
				return new GuiHydraulicSeparator(
						new ContainerHydraulicSeparator(player.inventory,
								(TileEntityHydraulicSeparator) tileEntity));
			} else if (ID == RTG) {
				return new GuiRTG(
						new ContainerRTG(player.inventory,
								(TileEntityRTG) tileEntity));
			} else if (ID == CHEMICAL_SEPARATOR) {
				return new GuiChemicalSeparator(
						new ContainerChemicalSeparator(player.inventory,
						(TileEntityChemicalSeparator) tileEntity));
			} else if (ID == CENTRIFUGE) {
				return new GuiCentrifuge(
						new ContainerCentrifuge(player.inventory,
						(TileEntityCentrifuge) tileEntity));
			}
		}
		return null;
	}
}
