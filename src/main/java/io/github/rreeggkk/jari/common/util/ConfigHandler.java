package io.github.rreeggkk.jari.common.util;

/*
 * Creation and usage of the config file.
 */

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;
	
	
	public static double RTG_MAX_WEIGHT = 1000;
	public static double RTGEnergyMultiplier = 0.2;

	// Sections to add to the config

	// Options in the config

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void syncConfig() {
		RTG_MAX_WEIGHT = config.get(Configuration.CATEGORY_GENERAL + ".balancing.rtg", "RTG_Max_Weight", 1000).getDouble();
		RTGEnergyMultiplier = config.get(Configuration.CATEGORY_GENERAL + ".balancing.rtg", "Energy_Multiplier", 0.2).getDouble();
		config.save();
	}
}
