package it.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Vector;
import java.util.Date;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.utilities.DatabaseManager;
import it.utilities.Utilities;
import it.configuration.Configuration;
import it.configuration.ErrorManager;

@Service
public class StatsService {
	
	// attributi
	private DatabaseManager databaseManager;
	
	boolean requirePressure;
	boolean requireHumidity;
	boolean requireTemperature;
	boolean requireVisibility;
	
	int periodicityValue;
	
	Date startDate;
	Date endDate;
	
	// metodi
	public Object getData(Vector<String> cityList, Vector<String> requestedWeather, String sortingType,
			String periodicity, Vector<String> dateSpan) {		
		
		boolean periodicityIsSet = false;
		try {
			periodicityIsSet = this.checkDateSpan(dateSpan);
			
		} catch (InvalidParameterException e3) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: periodicity is not valid - or - max size for 'datespan' is 3");
		} catch (java.text.ParseException e3) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: date format is not valid");
		}
		
		try {
			this.checkRequestedWeather(requestedWeather);
			if (!periodicityIsSet)
				periodicityValue = this.checkPeriodicity(periodicity);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'weather' is 4");
		} catch (NumberFormatException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: '"+periodicity+"' is not a valid periodicity");
		}
		
		
		
		databaseManager = new DatabaseManager();
		
		try {
			databaseManager.loadDatabase();
		} catch (FileNotFoundException e) {
			return new ErrorManager(e, "");
		} catch (IOException e) {	
			return new ErrorManager(e, "An error occurred while loading the database");
		} catch (ParseException e) {
			return new ErrorManager(e, "");
		}
		
		Object data = databaseManager.getLoadedData();
		
		// continua
		
		return new ErrorManager(new Exception(), "Funziona");
	}
	
	public void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException {
		if(requestedWeather == null || requestedWeather.get(0).equals("all"))
			this.setWeatherFields(true);
		
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
		if(startDate.compareTo(endDate) > 0) {
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
		requirePressure = value;
		requireHumidity = value;
		requireTemperature = value;
		requireVisibility = value;
	}
	
}
