package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.HashMap;
import java.util.Map;

import org.apfloat.Apfloat;

public class RadiumProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public RadiumProvider(Apfloat spontFiss, double fissEn, Isotope isotope) {
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
	public Map<String, Apfloat> doFission(FissionMode fiss,
			Apfloat amountFissioned) {
		switch (isotope) {
			case R224:
			{
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Radon-220", amountFissioned.divide(new Apfloat(1.1)));
				return map;
			}
			case R228:
			{
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Radium-224", amountFissioned.divide(new Apfloat(1.1)));
				return map;

			}
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
			case R224:
				return 224;
			case R228:
				return 228;
			default:
				return 0;
		}
	}

	@Override
	public boolean isSameElementAs(IElementProvider other) {
		return other instanceof RadiumProvider;
	}

	public enum Isotope {
		R224, R228;
	}
}
