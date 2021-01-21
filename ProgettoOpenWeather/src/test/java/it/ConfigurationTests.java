package it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.configuration.ErrorManager;

public class ConfigurationTests {
	
	private ErrorManager errorManager;
	
	@BeforeEach
	public void setUp() {
		errorManager = new ErrorManager(new ClassCastException(), "Test di ErrorManager", false);
	}
	
	@AfterEach
	public void tearDown() {}
	
	@Test
	@DisplayName("ErrorManager Test")
	public void errorManagerTest() {
		assertEquals(304, errorManager.getErrorId());
		assertNotEquals("Internal Error", errorManager.getMessage());
		assertEquals("Test di ErrorManager", errorManager.getMessage());
	}
}
