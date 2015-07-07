package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.HashMap;
import java.util.Map;

public class RadiumProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public RadiumProvider(double spontFiss, double fissEn, Isotope isotope) {
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
			case R224:
			{
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Radon-220", amountFissioned / 1.1);
				return map;
			}
			case R228:
			{
				HashMap<String, Double> map = new HashMap<String, Double>();
				map.put("Radium-224", amountFissioned / 1.1);
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
			case R224:
				return 224;
			case R228:
				return 228;
			default:
				return 0;
		}
	}

	public enum Isotope {
		R224, R228;
	}
}
