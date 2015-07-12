package io.github.rreeggkk.jari.common.elements.provider;

import io.github.rreeggkk.jari.common.elements.FissionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.apfloat.Apfloat;

public class IngotProvider extends IElementProvider.BaseProvider {

	private ItemStack mat;
	private double amt;
	
	private static ArrayList<ItemStack> mats = new ArrayList<ItemStack>();

	public IngotProvider(ItemStack mat, double amtPer) {
		super(new Apfloat(0f), 0, Double.MAX_VALUE, 0);
		this.mat = mat;
		mats.add(mat);
		this.amt = amtPer;
	}
	
	public static ArrayList<ItemStack> getMats() {
		return mats;
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
		return 0;
	}
	
	public ItemStack getMat() {
		return mat;
	}
	
	public double getAmt() {
		return amt;
	}

	@Override
	public boolean isSameElementAs(IElementProvider other) {
		return false;
	}
}
