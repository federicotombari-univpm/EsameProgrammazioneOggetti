package it.utilities;

import java.util.Date;
import java.util.List;

import it.configuration.Configuration;

public class Utilities {

	public static double roundDouble(double value) {
		return Math.round(value*1000)/1000;
	}
	
	public static Date getCurrentDate() {
		Date date = new Date();
		return date;
	}
	
	public static double calcAverageValue(List<Double> list) {
		return 0;
		
	}
	
	public static double calcVariance(List<Double> list, double average) {
		return 0;
		
	}
}
