package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.JARI;

import java.util.HashMap;
import java.util.Map;

public class PlutoniumProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public PlutoniumProvider(double spontFiss, Isotope isotope) {
		super(spontFiss, 620, Double.MAX_VALUE, 0);
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
		return isThermalNeutron ? JARI.random.nextDouble() + 0.5 : JARI.random
				.nextDouble() + 3.2;
	}

	@Override
	public Map<String, Double> doFission(boolean absorbed,
			double amountFissioned) {
		if (absorbed && isotope == Isotope.P238) {
			HashMap<String, Double> map = new HashMap<String, Double>();
			map.put("Plutonium-239", amountFissioned);
			return map;
		}
		if (JARI.random.nextBoolean()) {
			HashMap<String, Double> map = new HashMap<String, Double>();
			map.put("Krypton-89", amountFissioned / 2.01);
			map.put("Barium-144", amountFissioned / 2.01);
			return map;
		} else {
			HashMap<String, Double> map = new HashMap<String, Double>();
			map.put("Strontium-94", amountFissioned / 2.01);
			map.put("Xenon-140", amountFissioned / 2.01);
			return map;
		}
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
			case P238:
				return 238;
			case P239:
				return 239;
			default:
				return 0;
		}
	}
	public enum Isotope {
		P238, P239;
	}
}
