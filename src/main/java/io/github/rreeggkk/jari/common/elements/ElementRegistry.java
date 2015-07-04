package io.github.rreeggkk.jari.common.elements;

import io.github.rreeggkk.jari.common.elements.provider.FissionProductsProvider;
import io.github.rreeggkk.jari.common.elements.provider.IElementProvider;
import io.github.rreeggkk.jari.common.elements.provider.PlutoniumProvider;
import io.github.rreeggkk.jari.common.elements.provider.ThoriumProvider;
import io.github.rreeggkk.jari.common.elements.provider.UraniumProvider;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ElementRegistry {
	private static HashMap<String, IElementProvider> registry;

	static {
		registry = new HashMap<String, IElementProvider>();

		addElement("Krypton-89", new FissionProductsProvider(
				FissionProductsProvider.Product.K89));
		addElement("Strontium-94", new FissionProductsProvider(
				FissionProductsProvider.Product.S94));
		addElement("Xenon-140", new FissionProductsProvider(
				FissionProductsProvider.Product.X140));
		addElement("Barium-144", new FissionProductsProvider(
				FissionProductsProvider.Product.B144));

		addElement("Thorium-232", new ThoriumProvider(2e-6,
				ThoriumProvider.Isotope.T232));
		addElement("Thorium-233", new ThoriumProvider(8e-4,
				ThoriumProvider.Isotope.T233));

		addElement("Uranium-233", new UraniumProvider(9e-2,
				UraniumProvider.Isotope.U233));
		addElement("Uranium-235", new UraniumProvider(6e-2,
				UraniumProvider.Isotope.U235));
		addElement("Uranium-238", new UraniumProvider(3e-6,
				UraniumProvider.Isotope.U238));

		addElement("Plutonium-238", new PlutoniumProvider(3e-5,
				PlutoniumProvider.Isotope.P238));
		addElement("Plutonium-239", new PlutoniumProvider(1e-1,
				PlutoniumProvider.Isotope.P239));
	}

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
}
