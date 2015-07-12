package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.JARI;
import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.HashMap;
import java.util.Map;

import org.apfloat.Apfloat;

public class DecayChainProvider extends IElementProvider.BaseProvider {

	private Isotope isotope;

	public DecayChainProvider(Apfloat spontFiss, double fissEn, Isotope isotope) {
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
			case Rn220:
			{
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Polonium-216", amountFissioned.divide(new Apfloat(1.1)));
				return map;
			}
			case Po216:
			{
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Lead-212", amountFissioned.divide(new Apfloat(1.1)));
				return map;
			}
			case Pb212:
			{
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Bismuth-212", amountFissioned.divide(new Apfloat(1.1)));
				return map;
			}
			case Bi212:
			{
				double pol = JARI.random.nextDouble();
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Polonium-212", amountFissioned.multiply(new Apfloat(pol)).divide(new Apfloat(1.1)));
				map.put("Thallium-208", amountFissioned.multiply(new Apfloat(1).subtract(new Apfloat(pol))).divide(new Apfloat(1.1)));
				return map;
			}
			case Po212:
			{
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Lead-208", amountFissioned.divide(new Apfloat(1.1)));
				return map;
			}
			case Tl208:
			{
				HashMap<String, Apfloat> map = new HashMap<String, Apfloat>();
				map.put("Lead-208", amountFissioned.divide(new Apfloat(1.1)));
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

	@Override
	public boolean isSameElementAs(IElementProvider other) {
		if (!(other instanceof DecayChainProvider)) {
			return false;
		}
		DecayChainProvider dcp = (DecayChainProvider)other;

		switch (dcp.isotope) {
			case Po212:
			case Po216:
				return this.isotope == Isotope.Po212 || this.isotope == Isotope.Po216;
			default:
				return false;
		}
	}

	public enum Isotope {
		Rn220, Po216, Pb212, Bi212, Po212, Tl208;
	}
}
