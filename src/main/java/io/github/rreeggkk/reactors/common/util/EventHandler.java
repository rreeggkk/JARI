package io.github.rreeggkk.reactors.common.util;

/*
 * Class for most of your events to be registered in.
 * Remember that there are two different registries for Events. This one will not work for everything.
 */

import io.github.rreeggkk.reactors.Rreeactors;
import io.github.rreeggkk.reactors.common.reference.ModInformation;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.modID.equals(ModInformation.ID)) {
			ConfigHandler.syncConfig();
			Rreeactors.logger.info(TextHelper.localize("info." + ModInformation.ID + ".console.config.refresh"));
		}
	}
}
