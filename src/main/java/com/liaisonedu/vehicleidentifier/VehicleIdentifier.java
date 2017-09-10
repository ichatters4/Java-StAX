
package com.liaisonedu.vehicleidentifier;

import java.io.*;
import java.util.*;


import javax.xml.stream.*;
import javax.xml.stream.events.*;


public class VehicleIdentifier {

	/*
	 * Once a vehicle is identified (by type) this map will store the corresponding  
	 * vehicles of that type.
	 * 
	 * One optimization we could do is to just store the vehicle IDs..
	 */
	private static HashMap<VehicleType, ArrayList<Vehicle>> mapOfVehiclesByType;
	
    private static String readTextAfterXMLTag(XMLEventReader xmlEventReader, String tag) throws XMLStreamException {
    	return (xmlEventReader.nextEvent().asCharacters().getData().trim());
    }
	
	/*
	 * This method will return if the string in the XML file match the types we expect.
	 * If not, we will reject the token as an invalid XML file
	 */
	private static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		for (E e : enumClass.getEnumConstants()) {
			if (e.name().equalsIgnoreCase(value)) {
				return true; 
			}
		}
		return false;
	}
	
    private static VehicleFrame readVehicleFrame(XMLEventReader xmlEventReader) throws XMLStreamException {
		while (xmlEventReader.hasNext()) {
        	XMLEvent xmlEvent = xmlEventReader.nextEvent();
			int eventType = xmlEvent.getEventType();
			String frameMaterial = null;
			switch (eventType) {
			case XMLStreamReader.START_ELEMENT:
				StartElement startElement = xmlEvent.asStartElement();
				String elementName = startElement.getName().getLocalPart();
				if (elementName.equalsIgnoreCase("material")) {
					frameMaterial = readTextAfterXMLTag(xmlEventReader, "material");
        			if (!isInEnum(frameMaterial, VehicleFrame.class)) {
        				throw new XMLStreamException("Invalid token (Vehicle frame meterial) found in the vehicle XML file: " + frameMaterial);
        			}
        			return VehicleFrame.valueOf(frameMaterial.toUpperCase());
				}
				break;
			case XMLStreamReader.END_ELEMENT:
				break;
			}
	    }
		throw new XMLStreamException("Premature end of file");
    }
    
	private static List<Wheel> readWheels(XMLEventReader xmlEventReader) throws XMLStreamException {
		List<Wheel> wheels = new ArrayList<>();
		String elementName = null;
        while (xmlEventReader.hasNext()) {
        	XMLEvent xmlEvent = xmlEventReader.nextEvent();
			int eventType = xmlEvent.getEventType();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
    				StartElement startElement = xmlEvent.asStartElement();
    				elementName = startElement.getName().getLocalPart();
                    if (elementName.equalsIgnoreCase("wheel")) {
                    	wheels.add(readWheel(xmlEventReader));
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
					EndElement endElement = xmlEvent.asEndElement();
					elementName = endElement.getName().getLocalPart();
					if (elementName.equalsIgnoreCase("wheels")) {
						return wheels;
					}
            }
        }
        throw new XMLStreamException("Premature end of file");
	}
	
	private static WheelPosition returnWheelPosition(String wheelPosition) throws XMLStreamException {
		if (wheelPosition.equalsIgnoreCase("front")) {
			return WheelPosition.FRONT;
		} else if (wheelPosition.equalsIgnoreCase("left front")) {
			return WheelPosition.LEFTFRONT;
		} else if (wheelPosition.equalsIgnoreCase("right front")) {
			return WheelPosition.RIGHTFRONT;
		} else if (wheelPosition.equalsIgnoreCase("left rear")) {
			return WheelPosition.LEFTREAR;
		} else if (wheelPosition.equalsIgnoreCase("right rear")) {
			return WheelPosition.RIGHTREAR;
		} else if (wheelPosition.equalsIgnoreCase("rear")) {
			return WheelPosition.REAR;
		} else {
			throw new XMLStreamException("Error in XML file...Please verify all tags..See below details..");
		}
	}
	
	private static Wheel readWheel(XMLEventReader xmlEventReader) throws XMLStreamException {
		Wheel wheel = new Wheel();
		String wheelMaterial = null;
		String wheelPosition = null;
		String elementName = null;
		while (xmlEventReader.hasNext()) {
        	XMLEvent xmlEvent = xmlEventReader.nextEvent();
			int eventType = xmlEvent.getEventType();
			switch (eventType) {
				case XMLStreamReader.START_ELEMENT:
					StartElement startElement = xmlEvent.asStartElement();
					elementName = startElement.getName().getLocalPart();
					if (elementName.equalsIgnoreCase("material")) {
						wheelMaterial = readTextAfterXMLTag(xmlEventReader, "material");
	        			if (!isInEnum(wheelMaterial, WheelMaterial.class)) {
	        				throw new XMLStreamException("Invalid token (Wheel meterial) found in the vehicle XML file: " + wheelMaterial);
	        			}
	        			wheel.setMaterial(WheelMaterial.valueOf(wheelMaterial.toUpperCase()));
					} else if (elementName.equalsIgnoreCase("position")) {
						wheelPosition = readTextAfterXMLTag(xmlEventReader, "position");
						wheel.setWheelPosition(returnWheelPosition(wheelPosition));
					}
					break;
				case XMLStreamReader.END_ELEMENT:
					EndElement endElement = xmlEvent.asEndElement();
					elementName = endElement.getName().getLocalPart();
					if (elementName.equalsIgnoreCase("wheel")) {
						return wheel;
					}
			}
		}
		throw new XMLStreamException("Premature end of file");
	}
	
	
	/* 
	 * This method will read the contents of the XML file enclosed between the tags
	 * <vehicle>
	 * .
	 * .
	 * </vehicle>
	 */
	private static Vehicle readVehicle(XMLEventReader xmlEventReader) throws XMLStreamException {
		Vehicle vehicle = new Vehicle();
		List<Wheel> wheels = null;
		String elementName = null;
		while (xmlEventReader.hasNext()) {
        	XMLEvent xmlEvent = xmlEventReader.nextEvent();
			int eventType = xmlEvent.getEventType();
			switch (eventType) {
				case XMLStreamReader.START_ELEMENT:
					StartElement startElement = xmlEvent.asStartElement();
					elementName = startElement.getName().getLocalPart();
					if (elementName.equalsIgnoreCase("id")) {
						xmlEvent = xmlEventReader.nextEvent();
						String vehicleId = xmlEvent.asCharacters().getData();
						vehicle.setId(vehicleId);						
					} else if (elementName.equalsIgnoreCase("frame")) {
						vehicle.setFrame(readVehicleFrame(xmlEventReader));
					} else if (elementName.equalsIgnoreCase("wheels")) {
						wheels = readWheels(xmlEventReader);
						vehicle.setWheels(wheels);
					} else if (elementName.equalsIgnoreCase("powertrain")) {
						xmlEvent = xmlEventReader.nextEvent();
						String powertrain = xmlEvent.asCharacters().getData().trim();
	        			if (!isInEnum(powertrain, VehiclePowetrain.class)) {
	        				throw new XMLStreamException("Invalid token (powertrain) found in the vehicle XML file: " + powertrain);
	        			}
						vehicle.setPowerTrain(VehiclePowetrain.valueOf(powertrain.toUpperCase()));
					}
					break;
				case XMLStreamReader.END_ELEMENT:
					EndElement endElement = xmlEvent.asEndElement();
					elementName = endElement.getName().getLocalPart();
					if (elementName.equalsIgnoreCase("vehicle")) {
						return vehicle;
					}
			}
		}
		throw new XMLStreamException("Premature end of file");
	}
	
	/*
	 * Classify a vehicle type by its powertrain..
	 * 
	 * This is not a very elegant way of classifying a vehicle, but for now
	 * this will work.
	 */
	private static VehicleType classifyVehicleType(Vehicle vehicle) {
		VehicleType vehicleType = VehicleType.UNKNOWN;
		VehiclePowetrain powerTrain = vehicle.getPowerTrain();
		if (powerTrain == VehiclePowetrain.BERNOULLI) {
			vehicleType = VehicleType.HANG_GLIDER;
		} else if (powerTrain == VehiclePowetrain.HUMAN) {
			if (vehicle.getFrame() == VehicleFrame.PLASTIC) {
				vehicleType = VehicleType.BIG_WHEEL;
			} else if (vehicle.getFrame() == VehicleFrame.METAL) {
				vehicleType = VehicleType.BICYCLE;
			}
		} else if (powerTrain == VehiclePowetrain.COMBUSTION) {
			// Depending on the number of wheels it can be a motorcycle or a car
			if (vehicle.getWheels().size() == 2) {
				vehicleType = VehicleType.MOTORCYCLE;
			} else if (vehicle.getWheels().size() == 4) {
				vehicleType = VehicleType.CAR;
			}
		}
		return vehicleType;
	}
	
	private static HashMap<VehicleType, ArrayList<Vehicle>> readVehicles(XMLEventReader xmlEventReader) throws XMLStreamException {
		mapOfVehiclesByType = new HashMap<VehicleType, ArrayList<Vehicle>>();
        while (xmlEventReader.hasNext()) {
        	XMLEvent xmlEvent = xmlEventReader.nextEvent();
			int eventType = xmlEvent.getEventType();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
    				StartElement startElement = xmlEvent.asStartElement();
    				String elementName = startElement.getName().getLocalPart();
                    if (elementName.equalsIgnoreCase("vehicle")) {
                    	Vehicle vehicle = readVehicle(xmlEventReader);
                    	VehicleType vehicleType = classifyVehicleType(vehicle);
                    	/*
                    	 * If for some reason we cannot identify the vehicle,
                    	 * do not add it.
                    	 */
                    	if (vehicleType == VehicleType.UNKNOWN) {
                    		break;
                    	}
                    	vehicle.setType(vehicleType);
                    	ArrayList<Vehicle> vehicles = null;
                    	if (mapOfVehiclesByType.containsKey(vehicleType)) {
                    		vehicles = mapOfVehiclesByType.get(vehicleType);
                    	} else {
                    		vehicles = new ArrayList<Vehicle>();
                    	}
                    	vehicles.add(vehicle);
                		mapOfVehiclesByType.put(vehicleType, vehicles);
                    }
    				break;
                case XMLStreamReader.END_ELEMENT:
                	return mapOfVehiclesByType;
            }
        }
        throw new XMLStreamException("Premature end of file");
	}

	public static HashMap<VehicleType, ArrayList<Vehicle>> getVehiclesFromXMLFile(String fileName)  throws XMLStreamException {
		HashMap<VehicleType, ArrayList<Vehicle>> mapOfVehiclesByType = null;
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLEventReader xmlEventReader = null;
		try {
			xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
			mapOfVehiclesByType = readDocument(xmlEventReader);
		 } catch (FileNotFoundException | XMLStreamException e) {
			e.getMessage();
			e.printStackTrace();
		}  finally {
			if (xmlEventReader != null) {
				xmlEventReader.close();
			}
		}
		return mapOfVehiclesByType;
	}
	
	public static HashMap<VehicleType, ArrayList<Vehicle>> readDocument(XMLEventReader xmlEventReader) throws XMLStreamException {
		while (xmlEventReader.hasNext()) {
			XMLEvent xmlEvent = xmlEventReader.nextEvent();
			int eventType = xmlEvent.getEventType();
			switch (eventType) {
			case XMLStreamConstants.START_ELEMENT:
				StartElement startElement = xmlEvent.asStartElement();
				String elementName = startElement.getName().getLocalPart();
                if (elementName.equalsIgnoreCase("vehicles")) {
                	return readVehicles(xmlEventReader);
                }
				break;
			case XMLStreamConstants.END_ELEMENT:
				break;
			}
		}
		throw new XMLStreamException("Premature end of file");
	}
	
	public static void printVehicleReport(HashMap<VehicleType, ArrayList<Vehicle>> mapOfVehiclesByType) {
		if (mapOfVehiclesByType == null  || (mapOfVehiclesByType.keySet().size() == 0)) {
			System.out.println("There were no vehicles found in the XML file");
			return;
		}
		
		System.out.println();
		System.out.println("------------ VEHICLE SUMMARY ------------");
		System.out.println(" There were " + mapOfVehiclesByType.keySet().size() + " different type of vehicles found.");

		System.out.println();
		System.out.println("------------ VEHICLE DETAILS ------------");
		for (VehicleType key : mapOfVehiclesByType.keySet()) {
			System.out.println();
			System.out.println("Vehicle Type = " + key.toString());
			ArrayList<Vehicle> listOfVehicles = mapOfVehiclesByType.get(key);
			for (Vehicle vehicle : listOfVehicles) {
				vehicle.printDetails();
			}
		}
	}
	
	public static void main(String[] args) {
		String fileName = "c:\\ic\\vehicles.xml";
		try {
			mapOfVehiclesByType = getVehiclesFromXMLFile(fileName);
			printVehicleReport(mapOfVehiclesByType);
		} catch (XMLStreamException e) {
			System.out.println("Error in XML file...Please verify all tags..See below details..");
			System.out.println(e.getMessage());
		}
	}
}
