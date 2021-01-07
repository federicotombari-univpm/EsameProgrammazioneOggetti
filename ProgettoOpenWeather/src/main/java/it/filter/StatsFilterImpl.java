package it.filter;

import java.util.Date;
import java.util.Vector;

import it.configuration.Configuration;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.utilities.Utilities;


public class StatsFilterImpl implements StatsFilter {

	private boolean pressureRequired;
	private boolean humidityRequired;
	private boolean temperatureRequired;
	private boolean visibilityRequired;
	
	private int periodicityValue;
	
	private Date startDate;
	private Date endDate;
	
	public void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException {
		if(requestedWeather == null || requestedWeather.get(0).equals("all"))
			this.setWeatherFields(true);
		
		else if(requestedWeather.size() > 4)
			throw new InvalidParameterException();
		
		else
			this.setWeatherFields(false);
		
			for(String weatherInfo : requestedWeather) {
				if(weatherInfo.equals("pressure") || weatherInfo.equals("prs"))
					pressureRequired = true;
				else if(weatherInfo.equals("humidity") || weatherInfo.equals("hum"))
					humidityRequired = true;
				else if(weatherInfo.equals("temperature") || weatherInfo.equals("tmp"))
					temperatureRequired = true;
				else if(weatherInfo.equals("visibility") || weatherInfo.equals("vis"))
						visibilityRequired = true;
			}
	}
	
	public void checkPeriodicity(String periodicity) throws NumberFormatException {
		periodicityValue = 0;
		
		if (periodicity.equals("none"))
			periodicityValue = -1;
		else if (periodicity.equals("daily"))
			periodicityValue = 1;
		else if (periodicity.equals("weekly"))
			periodicityValue = 7;
		else if (periodicity.equals("monthly"))
			periodicityValue = 30;
		else
			periodicityValue = Integer.parseInt(periodicity);
	}
	
	public boolean checkDateSpan(Vector<String> dateSpan) throws InvalidParameterException, java.text.ParseException {
		
		endDate = Utilities.getCurrentDate();
		boolean periodicityIsSet = false;
		
		if(dateSpan == null) {
			startDate = Configuration.getFormatter().parse(Configuration.getDefaultDate());
		}
		
		else switch(dateSpan.size()) {
			case 3:
				this.checkPeriodicity(dateSpan.get(3));
				periodicityIsSet = true;
			case 2:
				endDate = Configuration.getFormatter().parse(dateSpan.get(1));
			case 1:
				startDate = Configuration.getFormatter().parse(dateSpan.get(0));
				break;
			default:
				throw new InvalidParameterException();
		}
		
		return periodicityIsSet;	
	}
	
	public void checkDates() throws InvalidParameterException, java.text.ParseException, DataNotFoundException {
		if (startDate.compareTo(endDate) > 0) {
			Date aux = new Date();
			aux = startDate;
			startDate = endDate;
			endDate = aux;
		}
		
		if (startDate.after(Utilities.getCurrentDate()))
			throw new InvalidParameterException();
		
		if (endDate.before(Configuration.getFormatter().parse(Configuration.getDefaultDate())))
			throw new DataNotFoundException();
	}
	
	public void setWeatherFields(boolean value) {
		pressureRequired = value;
		humidityRequired = value;
		temperatureRequired = value;	
		visibilityRequired = value;
	}

	// getter
	public boolean isPressureRequired() {
		return pressureRequired;
	}

	public boolean isHumidityRequired() {
		return humidityRequired;
	}

	public boolean isTemperatureRequired() {
		return temperatureRequired;
	}

	public boolean isVisibilityRequired() {
		return visibilityRequired;
	}

	public int getPeriodicityValue() {
		return periodicityValue;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

}
