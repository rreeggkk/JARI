package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.HashMap;
import java.util.Map;

import org.apfloat.Apfloat;

public class FissionProductsProvider extends IElementProvider.BaseProvider {

	private Product product;

	public FissionProductsProvider(Product product) {
		super(new Apfloat(0f), 0, Double.MAX_VALUE, 0);
		this.product = product;
	}

	@Override
	public double getNeutronHitChance(boolean isThermalNeutron) {
		return 3;
	}

	@Override
	public double getNeutronAbsorbChance(boolean isThermalNeutron) {
		return 75;
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
	public double getFissionEnergy() {
		return super.getFissionEnergy();
	}
	
	@Override
	public double getMolarMass() {
		switch (product) {
			case B144:
				return 144;
			case K89:
				return 89;
			case S94:
				return 94;
			case X140:
				return 140;
			default:
				return 0;
		}
	}

	@Override
	public boolean isSameElementAs(IElementProvider other) {
		return false;
	}

	public enum Product {
		K89, S94, X140, B144;
	}
}
