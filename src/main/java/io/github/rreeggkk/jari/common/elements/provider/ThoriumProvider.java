package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.HashMap;
import java.util.Map;

public class ThoriumProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public ThoriumProvider(double spontFiss, double fissEn, Isotope isotope) {
		super(spontFiss, fissEn, Double.MAX_VALUE, 0);
		this.isotope = isotope;
	}

	@Override
	public double getNeutronHitChance(boolean isThermalNeutron) {
		return isThermalNeutron ? 1 : 0.1;
	}

	@Override
	public double getNeutronAbsorbChance(boolean isThermalNeutron) {
		return isotope == Isotope.T232 ? isThermalNeutron ? 3 : 0.01 : 0;
	}

	@Override
	public double getNeutronFissionChance(boolean isThermalNeutron) {
		return isotope == Isotope.T232 ? 0 : isThermalNeutron ? 3 : 0.01;
	}

	@Override
	public double getOutputNeutrons(boolean isThermalNeutron) {
		return 0;
	}

	@Override
	public Map<String, Double> doFission(FissionMode fiss,
			double amountFissioned) {
		switch (fiss) {
			case ABSORB:
				if (isotope == Isotope.T232) {
					HashMap<String, Double> map = new HashMap<String, Double>();
					map.put("Thorium-233", amountFissioned);
					return map;
				}
				break;
			case FISSION:
				if (isotope == Isotope.T233) {
					HashMap<String, Double> map = new HashMap<String, Double>();
					map.put("Uranium-233", amountFissioned);
					return map;
				} else {
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
				}
			case DECAY:
				switch (isotope) {
					case T229:
					{
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Radium-228", amountFissioned / 1.1);
						return map;
					}
					case T230:
					{
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Radium-228", amountFissioned / 1.1);
						return map;

					}
					case T231:
					{
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Radium-228", amountFissioned / 1.1);
						return map;

					}
					case T232:
					{
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Radium-228", amountFissioned / 1.1);
						return map;
					}
					case T233:
					{
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Uranium-233", amountFissioned / 1.1);
						return map;
					}
					case T234:
					{
						HashMap<String, Double> map = new HashMap<String, Double>();
						map.put("Uranium-234", amountFissioned / 1.1);
						return map;
					}
				}
				break;
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
			case T229:
				return 229;
			case T230:
				return 230;
			case T231:
				return 231;
			case T232:
				return 232;
			case T233:
				return 233;
			case T234:
				return 234;
			default:
				return 0;
		}
	}

	public enum Isotope {
		T229, T230, T231, T232, T233, T234;
	}
}
