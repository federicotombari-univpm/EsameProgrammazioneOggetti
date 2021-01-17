	package it.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import it.configuration.Configuration;
import it.utilities.Utilities;


public class Operator {

	protected boolean filterByDateSpan;
	protected boolean filterByCityList;
	protected boolean sortFilteredData;
	
	protected boolean pressureRequested;
	protected boolean humidityRequested;
	protected boolean temperatureRequested;
	protected boolean visibilityRequested;
	
	protected int periodicityValue;
	
	protected Date startDate;
	protected Date endDate;
	
	protected Vector<String> cityList = null;
	
	protected String sortingType_main = null;
	protected String sortingType_weather = null;
	protected String sortingType_stats = null;
	
	// costruttore
	public Operator() throws ParseException {
		filterByDateSpan = true;
		filterByCityList = true;
		sortFilteredData = true;
		
		this.setWeatherBools(false);
		
		periodicityValue = Configuration.getDefaultPeriodicity();
		
		startDate = Configuration.getDateFormatter().parse(Configuration.getDefaultStartDate());
		endDate = Utilities.addDaysToDate(startDate, Configuration.getDefaultPeriodicity()-1);
	}
	
	
	// altri metodi
	public void setWeatherBools(boolean value) {
		pressureRequested = value;
		humidityRequested = value;
		temperatureRequested = value;
		visibilityRequested = value;
	}
	
}
