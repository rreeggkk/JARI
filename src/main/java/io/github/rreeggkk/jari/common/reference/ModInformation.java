package io.github.rreeggkk.jari.common.reference;

/*
 * Basic information your mod depends on.
 */

public class ModInformation {

	public static final String NAME = "JARI";
	public static final String ID = "jari";
	public static final String CHANNEL = "JARI";
	public static final String VERSION = "${version}";
	public static final String DEPEND = "required-after:ThermalFoundation;";
	public static final String CLIENTPROXY = "io.github.rreeggkk.jari.client.proxy.ClientProxy";
	public static final String COMMONPROXY = "io.github.rreeggkk.jari.common.proxy.CommonProxy";
	public static final String GUIFACTORY = "io.github.rreeggkk.jari.client.gui.config.ConfigGuiFactory";
}
