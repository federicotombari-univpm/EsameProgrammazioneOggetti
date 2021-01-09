package it.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;

import it.filter.StatsFilterImpl;
import it.utilities.DatabaseManager;
import it.configuration.ErrorManager;

@Service
public class StatsService {
	
	// attributi
	private DatabaseManager databaseManager = null;
	private StatsFilterImpl statsFilter = null;
	
	// metodi
	public Object getData(Vector<String> cityList, Vector<String> requestedWeather, String sortingType,
			String periodicity, Vector<String> dateSpan) {		
		
		statsFilter = new StatsFilterImpl();
		
		boolean periodicityIsSet = false;
		
		try {
			periodicityIsSet = statsFilter.checkDateSpan(dateSpan);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: periodicity is not valid - or - max size for 'datespan' is 3", false);
		} catch (java.text.ParseException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: date format is not valid", false);
		}
		
		try {
			statsFilter.checkDates();
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: defined datespan is a future period of time", false);
		} catch (java.text.ParseException e2) {
			return new ErrorManager(e2, "Internal Error", true);
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "No data available before 2021", false);
		}
		
		try {
			statsFilter.checkRequestedWeather(requestedWeather);
			if (!periodicityIsSet)
				statsFilter.checkPeriodicity(periodicity);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'weather' is 4", false);
		} catch (NumberFormatException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: '"+periodicity+"' is not a valid periodicity", false);
		}
		
		databaseManager = new DatabaseManager();
		
		try {
			databaseManager.loadDatabase();
		} catch (FileNotFoundException e1) {
			return new ErrorManager(e1, "", true);
		} catch (IOException e2) {	
			return new ErrorManager(e2, "An error occurred while loading the database", true);
		} catch (ParseException e3) {
			return new ErrorManager(e3, "", true);
		}
		
		JSONArray rawData = databaseManager.getLoadedData();
		
		try {
			statsFilter.filterData(rawData);
		} catch (java.text.ParseException e) {
			return new ErrorManager(e, "Internal Error", true);
		}
		
		
		
		// continua
		
		return new ErrorManager(new Exception(), "Funziona", false);
	}
	
}
