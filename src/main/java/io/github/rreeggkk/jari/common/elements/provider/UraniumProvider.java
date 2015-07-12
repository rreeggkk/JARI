package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.HashMap;
import java.util.Map;

import org.apfloat.Apfloat;

public class UraniumProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public UraniumProvider(Apfloat spontFiss, double fissEn, Isotope isotope) {
		super(spontFiss, fissEn, Double.MAX_VALUE, 0);
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
			case U234:
			case U235:
				return isThermalNeutron ? JARI.random.nextDouble() + 0.5
						: JARI.random.nextDouble() + 2.6;
			case U238:
				return JARI.random.nextDouble();
		}
		return 0;
	}

	@Override
	public Map<String, Apfloat> doFission(FissionMode fiss,
			Apfloat amountFissioned) {
		switch (fiss) {
			case ABSORB:
				switch (isotope) {
					case U233:
					case U234:
						HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
						map.put("Uranium-235", amountFissioned);
						return map;
					case U235:
						HashMap<String, Apfloat> map1 = new HashMap<String, Apfloat>();
						map1.put("Uranium-238", amountFissioned);
						return map1;
					case U238:
						HashMap<String, Apfloat> map11 = new HashMap<String, Apfloat>();
						if (JARI.random.nextBoolean()) {
							map11.put("Plutonium-238", amountFissioned);
						} else {
							map11.put("Plutonium-239", amountFissioned);
						}
						return map11;
				}
				break;
			case FISSION:
				if (JARI.random.nextBoolean()) {
					HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
					map.put("Krypton-89", amountFissioned.divide(new Apfloat(3.1)));
					map.put("Barium-144", amountFissioned.divide(new Apfloat(3.1)));
					return map;
				} else {
					HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
					map.put("Strontium-94", amountFissioned.divide(new Apfloat(3.1)));
					map.put("Xenon-140", amountFissioned.divide(new Apfloat(3.1)));
					return map;
				}
			case DECAY:
				switch (isotope) {
					case U233:
					{
						HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
						map.put("Thorium-229", amountFissioned.divide(new Apfloat(1.1)));
						return map;
					}
					case U234:
					{
						HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
						map.put("Thorium-230", amountFissioned.divide(new Apfloat(1.1)));
						return map;
					}
					case U235:
					{
						HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
						map.put("Thorium-231", amountFissioned.divide(new Apfloat(1.1)));
						return map;
					}
					case U238:
					{
						HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
						map.put("Thorium-234", amountFissioned.divide(new Apfloat(1.1)));
						return map;
					}
				}
				break;
				
		}
		return new HashMap<String, Apfloat>();
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
			case U234:
				return 234;
			case U235:
				return 235;
			case U238:
				return 238;
			default:
				return 0;
		}
	}

	@Override
	public boolean isSameElementAs(IElementProvider other) {
		return other instanceof UraniumProvider;
	}

	public enum Isotope {
		U233, U234, U235, U238;
	}
}
