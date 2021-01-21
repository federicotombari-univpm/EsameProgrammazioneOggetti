package it;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.exception.InvalidParameterException;
import it.model.Box;
import it.model.Coordinates;

public class ModelTests {

	private Box box;
	
	@BeforeEach
	public void setUp() {
		try {
			box = new Box(58.2, 26.9, 56.0, 25.7, -11);
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
	
	@AfterEach
	public void tearDown() {}
	
	@Test
	@DisplayName("Box Test")
	public void modelTest() {
		assertEquals(58.2, box.getMaxCoords().getLatitude());
		assertEquals(26.9, box.getMaxCoords().getLongitude());
		assertEquals(56.0, box.getMinCoords().getLatitude());
		assertEquals(25.7, box.getMinCoords().getLongitude());
		assertEquals(11, box.getZoom());
	}
	
	@Test
	@DisplayName("Coordinates Exception Test")
	public void exceptionTest() {
		Exception e = assertThrows(
				Exception.class, () -> new Coordinates(200, 50));
		
		assertTrue(e instanceof InvalidParameterException);
	}
}
