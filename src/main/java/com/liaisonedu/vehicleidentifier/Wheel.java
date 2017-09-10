package com.liaisonedu.vehicleidentifier;

public class Wheel {
	private WheelMaterial  material;
	private WheelPosition wheelPosition;

	public WheelMaterial getMaterial() {
		return material;
	}
	
	public void setMaterial(WheelMaterial material) {
		this.material = material;
	}
	
	public WheelPosition getWheelPosition() {
		return wheelPosition;
	}
	
	public void setWheelPosition(WheelPosition wheelPosition) {
		this.wheelPosition = wheelPosition;
	}
}
