package io.github.rreeggkk.jari.common.elements;

import io.github.rreeggkk.jari.common.elements.provider.DecayChainProvider;
import io.github.rreeggkk.jari.common.elements.provider.FissionProductsProvider;
import io.github.rreeggkk.jari.common.elements.provider.IElementProvider;
import io.github.rreeggkk.jari.common.elements.provider.IngotProvider;
import io.github.rreeggkk.jari.common.elements.provider.PlutoniumProvider;
import io.github.rreeggkk.jari.common.elements.provider.RadiumProvider;
import io.github.rreeggkk.jari.common.elements.provider.ThoriumProvider;
import io.github.rreeggkk.jari.common.elements.provider.UraniumProvider;
import io.github.rreeggkk.jari.common.util.ConfigHandler;
import io.github.rreeggkk.jari.common.util.NumUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import cofh.thermalfoundation.item.TFItems;

public class ElementRegistry {
	private static LinkedHashMap<String, IElementProvider> registry;

	static {
		registry = new LinkedHashMap<String, IElementProvider>();

		addElement("Krypton-89", new FissionProductsProvider(
				FissionProductsProvider.Product.K89));
		addElement("Strontium-94", new FissionProductsProvider(
				FissionProductsProvider.Product.S94));
		addElement("Xenon-140", new FissionProductsProvider(
				FissionProductsProvider.Product.X140));
		addElement("Barium-144", new FissionProductsProvider(
				FissionProductsProvider.Product.B144));
		
		addElement("Lead-208", new IngotProvider(TFItems.ingotLead, 750));

		addElement("Thallium-208", new DecayChainProvider(halfLifeToFC(new Apfloat(0.14)), 148,
				DecayChainProvider.Isotope.Tl208));

		addElement("Polonium-212", new DecayChainProvider(halfLifeToFC(new Apfloat(0.14)), 148,
				DecayChainProvider.Isotope.Po212));

		addElement("Bismuth-212", new DecayChainProvider(halfLifeToFC(minToSec(new Apfloat(61))), 153,
				DecayChainProvider.Isotope.Bi212));

		addElement("Lead-212", new DecayChainProvider(halfLifeToFC(hrToSec(new Apfloat(10.6))), 168,
				DecayChainProvider.Isotope.Pb212));

		addElement("Polonium-216", new DecayChainProvider(halfLifeToFC(new Apfloat(0.14)), 173,
				DecayChainProvider.Isotope.Po216));

		addElement("Radon-220", new DecayChainProvider(halfLifeToFC(new Apfloat(55)), 178,
				DecayChainProvider.Isotope.Rn220));

		addElement("Radium-224", new RadiumProvider(halfLifeToFC(dayToSec(new Apfloat(3.6))), 183,
				RadiumProvider.Isotope.R224));
		addElement("Radium-228", new RadiumProvider(halfLifeToFC(yearToSec(new Apfloat(5.7))), 188,
				RadiumProvider.Isotope.R228));

		addElement("Thorium-229", new ThoriumProvider(halfLifeToFC(yearToSec(new Apfloat(7340))), 180,
				ThoriumProvider.Isotope.T229));
		addElement("Thorium-230", new ThoriumProvider(halfLifeToFC(yearToSec(new Apfloat(75200))), 185,
				ThoriumProvider.Isotope.T230));
		addElement("Thorium-231", new ThoriumProvider(halfLifeToFC(minToSec(new Apfloat(25.5*60))), 190,
				ThoriumProvider.Isotope.T231));
		addElement("Thorium-232", new ThoriumProvider(halfLifeToFC(yearToSec(new Apfloat(1.405e10))), 193.3,
				ThoriumProvider.Isotope.T232));
		addElement("Thorium-233", new ThoriumProvider(halfLifeToFC(minToSec(new Apfloat(21.83))), 197.9,
				ThoriumProvider.Isotope.T233));
		addElement("Thorium-234", new ThoriumProvider(halfLifeToFC(minToSec(new Apfloat(24.1*60))), 200.2,
				ThoriumProvider.Isotope.T234));

		addElement("Uranium-233", new UraniumProvider(halfLifeToFC(yearToSec(new Apfloat(159200))), 197.9,
				UraniumProvider.Isotope.U233));
		addElement("Uranium-234", new UraniumProvider(halfLifeToFC(yearToSec(new Apfloat(246000))), 197.9,
				UraniumProvider.Isotope.U234));
		addElement("Uranium-235", new UraniumProvider(halfLifeToFC(yearToSec(new Apfloat(703800000))), 202.5,
				UraniumProvider.Isotope.U235));
		addElement("Uranium-238", new UraniumProvider(halfLifeToFC(yearToSec(new Apfloat(4.468 * NumUtil.BILLION))), 200.2,
				UraniumProvider.Isotope.U238));

		addElement("Plutonium-238", new PlutoniumProvider(halfLifeToFC(yearToSec(new Apfloat(87.7))), 207.1,
				PlutoniumProvider.Isotope.P238));
		addElement("Plutonium-239", new PlutoniumProvider(halfLifeToFC(yearToSec(new Apfloat(24100))), 207.1,
				PlutoniumProvider.Isotope.P239));
	}
	
	public static Apfloat halfLifeToFC(Apfloat halflifeSec) {
		halflifeSec = halflifeSec.divide(new Apfloat(ConfigHandler.HALF_LIFE_DIVISOR));
		return new Apfloat(1, 128).subtract(ApfloatMath.pow(new Apfloat(0.5), new Apfloat(1/20.0).divide(halflifeSec)));
	}
	
	public static Apfloat yearToSec(Apfloat year) {
		return dayToSec(year.multiply(new Apfloat(365)));
	}
	
	public static Apfloat dayToSec(Apfloat day) {
		return hrToSec(day.multiply(new Apfloat(24)));
	}
	
	public static Apfloat hrToSec(Apfloat hr) {
		return minToSec(hr.multiply(new Apfloat(60)));
	}
	
	public static Apfloat minToSec(Apfloat min) {
		return min.multiply(new Apfloat(60));
	}
	/*
	public static double secToYear(double sec) {
		return (((sec/60)/60)/24)/365;
	}*/

	public static void addElement(String string, IElementProvider provider) {
		registry.put(string, provider);
	}

	public static void removeElement(String string) {
		registry.remove(string);
	}

	public static IElementProvider getProviderForElement(String elementName) {
		return registry.get(elementName);
	}

	public static String getRegexStringList() {
		StringBuilder str = new StringBuilder("(");
		for (String s : registry.keySet()) {
			str.append(Pattern.quote(s));
			str.append("|");
		}
		str.append(")");
		return str.toString();
	}
	
	public static LinkedHashMap<String, IElementProvider> getRegistry() {
		return registry;
	}

	public static int getCount() {
		return registry.size();
	}
	
	public static String getFromIndex(int index) {
		return new ArrayList<String>(registry.keySet()).get(index);
	}

	public static int getIndex(String string) {
		return new ArrayList<String>(registry.keySet()).indexOf(string);
	}
}
