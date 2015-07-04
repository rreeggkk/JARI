package io.github.rreeggkk.jari.common.entity.tile;

import io.github.rreeggkk.jari.common.enums.RedstonePowerMode;

public interface IRedstoneControllable {

	public RedstonePowerMode getPowerMode();

	public void setPowerMode(RedstonePowerMode powerMode);
}
