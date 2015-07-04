package io.github.rreeggkk.jari.common.enums;

public enum RedstonePowerMode {

	REQUIRED_ON(0, "jari.gui.element.redstoneControl.high"), REQUIRED_OFF(1,
			"jari.gui.element.redstoneControl.low"), ALWAYS_OFF(2,
			"jari.gui.element.redstoneControl.never"), ALWAYS_ON(3,
			"jari.gui.element.redstoneControl.always");

	private int index;
	private String delocString;
	private static final int maxIndex = 4;

	private RedstonePowerMode(int index, String delocString) {
		this.index = index;
		this.delocString = delocString;
	}

	public int getIndex() {
		return index;
	}

	public String getDelocString() {
		return delocString;
	}

	public static RedstonePowerMode getFromIndex(int index) {
		for (RedstonePowerMode r : values()) {
			if (r.getIndex() == index) {
				return r;
			}
		}
		return null;
	}

	public static RedstonePowerMode getNext(RedstonePowerMode powerMode) {
		int indx = powerMode.getIndex() + 1;
		if (indx >= maxIndex) {
			indx -= maxIndex;
		}
		return getFromIndex(indx);
	}
}
