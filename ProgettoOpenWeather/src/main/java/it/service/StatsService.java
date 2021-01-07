package it.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Vector;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.exception.WebServiceException;
import it.filter.StatsFilterImpl;
import it.utilities.DatabaseManager;
import it.configuration.ErrorManager;

@Service
public class StatsService {
	
	// attributi
	private DatabaseManager databaseManager;
	private StatsFilterImpl statsFilter;
	
	// metodi
	public Object getData(Vector<String> cityList, Vector<String> requestedWeather, String sortingType,
			String periodicity, Vector<String> dateSpan) {		
		
		boolean periodicityIsSet = false;
		
		try {
			periodicityIsSet = statsFilter.checkDateSpan(dateSpan);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: periodicity is not valid - or - max size for 'datespan' is 3");
		} catch (java.text.ParseException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: date format is not valid");
		}
		
		try {
			statsFilter.checkDates();
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: defined datespan is a future period of time");
		} catch (java.text.ParseException e2) {
			return new ErrorManager(e2, "Internal Error");
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "No data available before 2021");
		}
		
		try {
			statsFilter.checkRequestedWeather(requestedWeather);
			if (!periodicityIsSet)
				statsFilter.checkPeriodicity(periodicity);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'weather' is 4");
		} catch (NumberFormatException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: '"+periodicity+"' is not a valid periodicity");
		}
		
		databaseManager = new DatabaseManager();
		
		try {
			databaseManager.loadDatabase(true);
		} catch (FileNotFoundException e1) {
			return new ErrorManager(e1, "");
		} catch (IOException e2) {	
			return new ErrorManager(e2, "An error occurred while loading the database");
		} catch (ParseException e3) {
			return new ErrorManager(e3, "");
		} catch (WebServiceException e4) {
			return new ErrorManager(e4, "The database is being updated, please try again in a few seconds");
		}
		
		Object data = databaseManager.getLoadedData();
		
		// continua
		
		return new ErrorManager(new Exception(), "Funziona");
	}
	
}
