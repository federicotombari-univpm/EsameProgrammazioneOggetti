	package it.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.configuration.Configuration;


public class Operator {

	protected boolean filterByDateSpan;
	protected boolean filterByCityList;
	protected boolean filterByPeriodicity;
	
	protected boolean pressureRequested;
	protected boolean humidityRequested;
	protected boolean temperatureRequested;
	protected boolean visibilityRequested;
	
	protected int periodicityValue;
	
	protected Vector<String> cityList;
	
	protected Date startDate;
	protected Date endDate;
	
	// costruttore
	public Operator() {
		filterByDateSpan = true;
		filterByCityList = true;
		filterByPeriodicity = true;
		
		this.setWeatherBools(false);
		
		periodicityValue = 0;
		cityList = null;
		startDate = null;
		endDate = null;
	}
	
	
	// altri metodi
	public void setWeatherBools(boolean value) {
		pressureRequested = value;
		humidityRequested = value;
		temperatureRequested = value;
		visibilityRequested = value;
	}
	
	public Date readAndParseTimestamp(JSONArray data, int index) throws ParseException {
		JSONObject dataElement = (JSONObject) data.get(index);
		String timestamp = (String) dataElement.get("timestamp");
		return Configuration.getDateFormatter().parse(timestamp);
	}
	
}
