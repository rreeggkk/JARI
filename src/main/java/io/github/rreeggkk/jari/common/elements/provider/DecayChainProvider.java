package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.HashMap;
import java.util.Map;

public class DecayChainProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public DecayChainProvider(double spontFiss, double fissEn, Isotope isotope) {
		super(spontFiss, fissEn, Double.MAX_VALUE, 0);
		this.isotope = isotope;
	}

	@Override
	public double getNeutronHitChance(boolean isThermalNeutron) {
		return isThermalNeutron ? 1 : 0.1;
	}

	@Override
	public double getNeutronAbsorbChance(boolean isThermalNeutron) {
		return 0;
	}

	@Override
	public double getNeutronFissionChance(boolean isThermalNeutron) {
		return 0;
	}

	@Override
	public double getOutputNeutrons(boolean isThermalNeutron) {
		return 0;
	}

	@Override
	public Map<String, Double> doFission(FissionMode fiss,
			double amountFissioned) {
		switch (isotope) {
			case Rn220:
			{
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Polonium-216", amountFissioned / 1.1);
				return map;
			}
			case Po216:
			{
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Lead-212", amountFissioned / 1.1);
				return map;
			}
			case Pb212:
			{
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Bismuth-212", amountFissioned / 1.1);
				return map;
			}
			case Bi212:
			{
				double pol = JARI.random.nextDouble();
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Polonium-212", (amountFissioned * pol) / 1.1);
				map.put("Thallium-208", (amountFissioned * (1 - pol)) / 1.1);
				return map;
			}
			case Po212:
			{
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Lead-208", amountFissioned / 1.1);
				return map;
			}
			case Tl208:
			{
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Lead-208", amountFissioned / 1.1);
				return map;
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
			case Rn220:
				return 220;
			case Po216:
				return 216;
			case Pb212:
			case Bi212:
			case Po212:
				return 212;
			case Tl208:
				return 208;
		}
		return 0;
	}

	public enum Isotope {
		Rn220, Po216, Pb212, Bi212, Po212, Tl208;
	}
}
