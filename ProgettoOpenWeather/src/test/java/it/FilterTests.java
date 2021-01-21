package it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.configuration.Configuration;
import it.exception.InvalidParameterException;
import it.filter.CheckerImpl;

public class FilterTests {
	private CheckerImpl checker1;
	private CheckerImpl checker2;
	private Vector<String> cityList;

	@BeforeEach
	public void setUp() {
		try {
			checker1 = new CheckerImpl();
			checker2 = new CheckerImpl();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		cityList = new Vector<String>();
		cityList.add("Ascoli Piceno");
		cityList.add("Urbino");
		cityList.add("Macerata");
		
		try {
			Configuration.initializeConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			checker2.checkCityList(cityList);
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
	
	@AfterEach
	public void tearDown() {}
	
	@Test
	@DisplayName("CityList filter Test 1")
	public void cityListNull() {
		assertNull(checker1.getCityList());
	}
	
	@Test
	@DisplayName("CityList filter Test 2")
	public void cityList() {
		assertEquals(2, checker2.getCityList().size());
		assertEquals("Ascoli Piceno", checker2.getCityList().get(0));
		assertEquals("Macerata", checker2.getCityList().get(1));
	}
}
