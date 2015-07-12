package io.github.rreeggkk.jari.common.util;

/*
 * Creation and usage of the config file.
 */

import java.io.File;

import org.apfloat.Apfloat;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static final Apfloat MIN_CUTOFF = new Apfloat(1e-30);

	public static Configuration config;
	
	public static double CENTRIFUGE_POWER_USE_MULTIPLIER = 1;
	
	public static double HALF_LIFE_DIVISOR;
	
	public static double RTG_MAX_WEIGHT;
	public static double RTGEnergyMultiplier;
	
	//public static double SMALL_CUTOFF_POINT = 1E-15;

	// Sections to add to the config

	// Options in the config

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void syncConfig() {
		HALF_LIFE_DIVISOR = config.get(Configuration.CATEGORY_GENERAL + ".values", "HALF_LIFE_DIVISOR", 100,
				"Set the number by which to divide IRL halflives for game. REQUIRES A RESTART.").getDouble();
		
		CENTRIFUGE_POWER_USE_MULTIPLIER = config.get(Configuration.CATEGORY_GENERAL + ".balancing.centrifuge", "Energy Use Multiplier", 1).getDouble();
		
		RTG_MAX_WEIGHT = config.get(Configuration.CATEGORY_GENERAL + ".balancing.rtg", "RTG_Max_Weight", 20000).getDouble();
		RTGEnergyMultiplier = config.get(Configuration.CATEGORY_GENERAL + ".balancing.rtg", "Energy_Multiplier", 0.2).getDouble();
		config.save();
	}
}
