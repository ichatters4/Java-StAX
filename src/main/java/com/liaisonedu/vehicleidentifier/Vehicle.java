package com.liaisonedu.vehicleidentifier;

import java.util.List;

public class Vehicle {
	private String id;
	private VehicleFrame frame;
	private VehicleType type;
	private VehiclePowetrain powerTrain;

	private List<Wheel> wheels;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public VehicleFrame getFrame() {
		return frame;
	}
	
	public void setFrame(VehicleFrame frame) {
		this.frame = frame;
	}
	
	public VehicleType getType() {
		return type;
	}
	
	public void setType(VehicleType type) {
		this.type = type;
	}
	
	public VehiclePowetrain getPowerTrain() {
		return powerTrain;
	}

	public void setPowerTrain(VehiclePowetrain powerTrain) {
		this.powerTrain = powerTrain;
	}
	
	public List<Wheel> getWheels() {
		return wheels;
	}
	
	public int getNumberOfWheels() {
		return wheels.size();
	}

	public void setWheels(List<Wheel> wheels) {
		this.wheels = wheels;
	}
	
	public void printDetails() {
		System.out.println();
		System.out.println("Vehicle ID:          " + getId());
		System.out.println("Vehicle Frame:       " + getFrame().toString());
		System.out.println("Powertrain:          " + getPowerTrain().toString());
		
		System.out.println();
		System.out.println("Number of Wheels:    " + getNumberOfWheels());
		for (Wheel wheel : getWheels()) {
			System.out.println();
			System.out.println("\tWheel material: " + wheel.getMaterial().toString());
			System.out.println("\tWheel position: " + wheel.getWheelPosition().toString());
		}
	}
}
