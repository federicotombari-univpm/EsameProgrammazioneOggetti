package it.service;

import java.util.Vector;

import org.springframework.stereotype.Service;

import it.exception.InvalidParameterException;

import it.configuration.ErrorManager;

@Service
public class StatsService {
	
	// attributi
	boolean requirePressure = false;
	boolean requireHumidity = false;
	boolean requireTemperature = false;
	boolean requireVisibility = false;
	int periodicityValue = 0;
	
	// metodi
	public void setWeatherFields(boolean value) {
		requirePressure = value;
		requireHumidity = value;
		requireTemperature = value;
		requireVisibility = value;
	}
	
	public Object getData(Vector<String> cityList, Vector<String> requestedWeather, String sortingType,
			String periodicity, Vector<String> dateSpan) {
		
		
		
		try {
			this.checkRequestedWeather(requestedWeather);
			periodicityValue = this.checkPeriodicity(periodicity);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'weather' is 4");
		} catch (NumberFormatException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: '"+periodicity+"' is not a valid periodicity");
		}
		
		
		
		
		return new ErrorManager(new Exception(), "Funziona");
	}
	
	public void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException {
		if(requestedWeather == null || requestedWeather.get(0).equals("all")) {
			this.setWeatherFields(true);
		}
		
		else if(requestedWeather.size() > 4)
			throw new InvalidParameterException();
		
		else
			this.setWeatherFields(false);
		
			for(String weatherInfo : requestedWeather) {
				if(weatherInfo.equals("pressure") || weatherInfo.equals("prs"))
					requirePressure = true;
				else if(weatherInfo.equals("humidity") || weatherInfo.equals("hum"))
					requireHumidity = true;
				else if(weatherInfo.equals("temperature") || weatherInfo.equals("tmp"))
					requireTemperature = true;
				else if(weatherInfo.equals("visibility") || weatherInfo.equals("vis"))
						requireVisibility = true;
			}
	}
	
	public int checkPeriodicity(String periodicity) throws NumberFormatException {
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
		return periodicityValue;
	}
	
}
