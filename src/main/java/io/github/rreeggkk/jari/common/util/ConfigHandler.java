package io.github.rreeggkk.jari.common.util;

/*
 * Creation and usage of the config file.
 */

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;
	
	
	public static double RTG_MAX_WEIGHT;
	public static double RTGEnergyMultiplier;
	public static double HALF_LIFE_DIVISOR;

	// Sections to add to the config

	// Options in the config

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void syncConfig() {
		HALF_LIFE_DIVISOR = config.get(Configuration.CATEGORY_GENERAL + ".values", "HALF_LIFE_DIVISOR", 1000).getDouble();
		
		RTG_MAX_WEIGHT = config.get(Configuration.CATEGORY_GENERAL + ".balancing.rtg", "RTG_Max_Weight", 5000).getDouble();
		RTGEnergyMultiplier = config.get(Configuration.CATEGORY_GENERAL + ".balancing.rtg", "Energy_Multiplier", 0.2).getDouble();
		config.save();
	}
}
