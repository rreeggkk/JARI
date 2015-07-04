package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.JARI;

import java.util.HashMap;
import java.util.Map;

public class UraniumProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public UraniumProvider(double spontFiss, Isotope isotope) {
		super(spontFiss, 400, Double.MAX_VALUE, 0);
		this.isotope = isotope;
	}

	@Override
	public double getNeutronHitChance(boolean isThermalNeutron) {
		return isThermalNeutron ? 0.4 : 0.1;
	}

	@Override
	public double getNeutronAbsorbChance(boolean isThermalNeutron) {
		return isThermalNeutron ? 2 : 0.07;
	}

	@Override
	public double getNeutronFissionChance(boolean isThermalNeutron) {
		return isThermalNeutron ? 0.00002 : 0.3;
	}

	@Override
	public double getOutputNeutrons(boolean isThermalNeutron) {
		switch (isotope) {
			case U233:
				return isThermalNeutron ? JARI.random.nextDouble() + 2.6
						: JARI.random.nextDouble() + 0.5;
			case U235:
				return isThermalNeutron ? JARI.random.nextDouble() + 0.5
						: JARI.random.nextDouble() + 2.6;
			case U238:
				return JARI.random.nextDouble();
		}
		return 0;
	}

	@Override
	public Map<String, Double> doFission(boolean absorbed,
			double amountFissioned) {
		if (absorbed) {
			switch (isotope) {
				case U233:
					HashMap<String, Double> map = new HashMap<String, Double>();
					map.put("Uranium-235", amountFissioned);
					return map;
				case U235:
					HashMap<String, Double> map1 = new HashMap<String, Double>();
					map1.put("Uranium-238", amountFissioned);
					return map1;
				case U238:
					HashMap<String, Double> map11 = new HashMap<String, Double>();
					if (JARI.random.nextBoolean()) {
						map11.put("Plutonium-238", amountFissioned);
					} else {
						map11.put("Plutonium-239", amountFissioned);
					}
					return map11;
			}
		} else {
			switch (isotope) {
				case U233:
				case U235:
				case U238:
					if (JARI.random.nextBoolean()) {
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Krypton-89", amountFissioned / 3.1);
						map.put("Barium-144", amountFissioned / 3.1);
						return map;
					} else {
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Strontium-94", amountFissioned / 3.1);
						map.put("Xenon-140", amountFissioned / 3.1);
						return map;
					}
				default:
					break;
			}
		}
		return new HashMap<String, Double>();
	}

	@Override
	public Map<String, Double> getFusionRequirements() {
		return null;
	}

	@Override
	public Map<String, Double> getFusionOutput() {
		return null;
	}

	@Override
	public double getMolarMass() {
		switch (isotope) {
			case U233:
				return 233;
			case U235:
				return 235;
			case U238:
				return 238;
			default:
				return 0;
		}
	}

	public enum Isotope {
		U233, U235, U238;
	}
}
