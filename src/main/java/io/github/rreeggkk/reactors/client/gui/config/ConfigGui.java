package io.github.rreeggkk.reactors.client.gui.config;

/*
 * Creates a config GUI for your mod. This requires an mcmod.info file with the correct modid. These are dummy sections that don't do anything.
 */

import io.github.rreeggkk.reactors.common.reference.ModInformation;
import io.github.rreeggkk.reactors.common.util.TextHelper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class ConfigGui extends GuiConfig {

	public ConfigGui(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), ModInformation.ID, false, false, TextHelper.localize("gui." + ModInformation.ID + ".config.title"));
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements(GuiScreen parent) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		// Adds sections declared in ConfigHandler. toLowerCase() is used because the configuration class automatically does this, so must we.

		return list;
	}
}