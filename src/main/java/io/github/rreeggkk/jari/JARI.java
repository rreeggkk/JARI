package io.github.rreeggkk.jari;

import io.github.rreeggkk.jari.client.creativetabs.CustomCreativeTab;
import io.github.rreeggkk.jari.common.block.BlockRecipeRegistry;
import io.github.rreeggkk.jari.common.block.BlockRegistry;
import io.github.rreeggkk.jari.common.command.MetalLumpCommand;
import io.github.rreeggkk.jari.common.gui.GuiHandler;
import io.github.rreeggkk.jari.common.item.ItemRecipeRegistry;
import io.github.rreeggkk.jari.common.item.ItemRegistry;
import io.github.rreeggkk.jari.common.proxy.CommonProxy;
import io.github.rreeggkk.jari.common.reference.ModInformation;
import io.github.rreeggkk.jari.common.util.ConfigHandler;
import io.github.rreeggkk.jari.common.util.EventHandler;
import io.github.rreeggkk.jari.common.util.OreDictHandler;
import io.github.rreeggkk.jari.common.util.OreGenData;
import io.github.rreeggkk.jari.common.util.RreeOreGenerator;
import io.github.rreeggkk.jari.common.util.TextHelper;

import java.util.Random;

import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 *
 * @author rreeggkk
 * 
 * The main file for the JARI mod.
 *
 */
@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, dependencies = ModInformation.DEPEND, guiFactory = ModInformation.GUIFACTORY)
public class JARI {

	// The mod's proxy
	@SidedProxy(clientSide = ModInformation.CLIENTPROXY, serverSide = ModInformation.COMMONPROXY)
	public static CommonProxy proxy;

	// The creative tab
	public static CustomCreativeTab tabRreeactors = new CustomCreativeTab(
			ModInformation.ID + ".creativeTab");
	// The logger
	public static Logger logger = LogManager.getLogger(ModInformation.NAME);
	// The random
	public static Random random = new Random();

	private RreeOreGenerator oreGenerator;

	// Mod instance
	@Instance
	public static JARI instance;

	/**
	 * Pre init method
	 * 
	 * @param event
	 *            FML's pre init event
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Log the beggining of preinit
		logger.info(TextHelper.localize("info." + ModInformation.ID
				+ ".console.load.preInit"));

		// Initialize or config
		ConfigHandler.init(event.getSuggestedConfigurationFile());

		// Load items and blocks
		ItemRegistry.registerItems();
		BlockRegistry.registerBlocks();

		// Set reactor tab icon
		tabRreeactors.setIcon(Item
				.getItemFromBlock(BlockRegistry.blockHydraulicSeparator));

		// Do ore dictionary registry
		OreDictHandler.registerOreDict();
		// Register the mod's event handler
		new EventHandler();
		// Register the mod's gui handler
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		// Register the mod's world generation handler
		GameRegistry.registerWorldGenerator(
				oreGenerator = new RreeOreGenerator(), 2);

		oreGenerator.addOreGenToSurface(new OreGenData(255, 0, 25, 3,
				BlockRegistry.blockRadioactiveStone));
	}

	/**
	 * Initialization method
	 * 
	 * @param event FML's initialization event
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// Log the beggining of init
		logger.info(TextHelper.localize("info." + ModInformation.ID
				+ ".console.load.init"));

	}

	/**
	 * Post initialization method
	 * 
	 * @param event FML's postinitialization event
	 */
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Log the beggining of postinit
		logger.info(TextHelper.localize("info." + ModInformation.ID
				+ ".console.load.postInit"));

		// Load item and block recipes
		ItemRecipeRegistry.registerItemRecipes();
		BlockRecipeRegistry.registerBlockRecipes();
	}

	/**
	 * Called when the server starts
	 * 
	 * @param event FML's server start event
	 */
	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		logger.info(TextHelper.localize("info." + ModInformation.ID
				+ ".console.load.serverStart"));
		event.registerServerCommand(new MetalLumpCommand());
	}
}
