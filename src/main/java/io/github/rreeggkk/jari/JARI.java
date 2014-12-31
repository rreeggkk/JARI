package io.github.rreeggkk.jari;

/*
 * Check all the classes for (hopefully) detailed descriptions of what it does. There will also be tidbits of comments throughout the codebase.
 * If you wish to add a description to a class, or extend/change an existing one, submit a PR with your changes.
 */

import io.github.rreeggkk.jari.client.creativetabs.CustomCreativeTab;
import io.github.rreeggkk.jari.client.gui.GuiHandler;
import io.github.rreeggkk.jari.common.block.BlockRecipeRegistry;
import io.github.rreeggkk.jari.common.block.BlockRegistry;
import io.github.rreeggkk.jari.common.item.ItemRecipeRegistry;
import io.github.rreeggkk.jari.common.item.ItemRegistry;
import io.github.rreeggkk.jari.common.proxy.CommonProxy;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import io.github.rreeggkk.jari.common.util.ConfigHandler;
import io.github.rreeggkk.jari.common.util.EventHandler;
import io.github.rreeggkk.jari.common.util.GenerationHandler;
import io.github.rreeggkk.jari.common.util.OreDictHandler;
import io.github.rreeggkk.jari.common.util.TextHelper;

import java.util.Random;

import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, dependencies = ModInformation.DEPEND, guiFactory = ModInformation.GUIFACTORY)
public class JARI {

	@SidedProxy(clientSide = ModInformation.CLIENTPROXY, serverSide = ModInformation.COMMONPROXY)
	public static CommonProxy proxy;

	public static CustomCreativeTab tabRreeactors = new CustomCreativeTab(
			ModInformation.ID + ".creativeTab");
	public static Logger logger = LogManager.getLogger(ModInformation.NAME);
	public static Random random = new Random();

	@Mod.Instance
	public static JARI instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info(TextHelper.localize("info." + ModInformation.ID
				+ ".console.load.preInit"));

		ConfigHandler.init(event.getSuggestedConfigurationFile());

		ItemRegistry.registerItems();
		BlockRegistry.registerBlocks();

		tabRreeactors.setIcon(Item
				.getItemFromBlock(BlockRegistry.blockHydraulicSeparator));

		OreDictHandler.registerOreDict();
		FMLCommonHandler.instance().bus().register(new EventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		GameRegistry.registerWorldGenerator(new GenerationHandler(), 2);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info(TextHelper.localize("info." + ModInformation.ID
				+ ".console.load.init"));

		ItemRecipeRegistry.registerItemRecipes();
		BlockRecipeRegistry.registerBlockRecipes();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		logger.info(TextHelper.localize("info." + ModInformation.ID
				+ ".console.load.postInit"));
	}
}
