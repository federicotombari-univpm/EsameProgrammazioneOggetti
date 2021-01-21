package it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.configuration.Configuration;
import it.utilities.ThreadManager;
import it.utilities.Utilities;

public class UtilitiesTests {

	private Vector<Double> values;
	private ThreadManager threadManager;
	
	@BeforeEach
	public void setUp() {
		values = new Vector<Double>();
		values.add(20.0);
		values.add(14.7);
		values.add(22.9);
		
		threadManager = new ThreadManager();
		Configuration.setDefaultThreadDelay(-1000);
	}
	
	@AfterEach
	public void tearDown() {}
	
	@Test
	@DisplayName("Average and Variance Test")
	public void statsTest() {
		assertEquals(19.2, Utilities.calcAverage(values));
		assertEquals(11.527, Utilities.calcVariance(values, 19.2));
	}
	
	@Test
	@DisplayName("Thread Test")
	public void threadTest() {
		
		Exception e = assertThrows(
				Exception.class, () -> threadManager.startThread(false));
		
		assertFalse(ThreadManager.isRunning());
		assertTrue(e instanceof IllegalArgumentException);
		
	}
}
