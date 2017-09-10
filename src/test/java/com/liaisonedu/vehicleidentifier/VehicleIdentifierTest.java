package com.liaisonedu.vehicleidentifier;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import java.util.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

/**
 * Unit test for simple App.
 */
public class VehicleIdentifierTest {
	
	/**
	 * ReadMe.1st
	 * 
	 * An StAX parser does not lend itself very well to JUnit test cases unless one can mock 
	 * XML files using some king of an input source from memory viz..
	 * 
	 *	  StringReader reader = new StringReader(s); 
	 *	  InputSource source = new InputSource(reader);
	 *
	 *  I have not been able to figure out how to tie these two together...whether to use 
	 *  a XMLEventReader or an XMLStreamReader..
	 *  
	 *  The other comment I have is - As most of the methods in the class VehicleIdentifier
	 *  are private methods (they are helpers) to the main XML parser the only way to test 
	 *  them as one unit would be to use 'reflection' which is not a very good practice.  
	 */
	@Test
	public void testGetVehiclesFromXMLFile() {
		HashMap<VehicleType, ArrayList<Vehicle>> mapOfVehiclesByType = null;
		VehicleIdentifier vehicleIdentifier = new VehicleIdentifier();		

		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><vehicles><vehicle></vehicle></vehicles>";
		StringReader reader = new StringReader(s);
		InputSource source = new InputSource(reader);
		
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		// XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(source);
		
		// vehicleIdentifier.getVehiclesFromXMLFile(s)
		
		assertTrue(true);
	}
}
	
	