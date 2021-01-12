package it.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.filter.CheckerImpl;
import it.filter.FiltratorImpl;
import it.filter.Operator;
import it.filter.SorterImpl;
import it.utilities.DatabaseManager;
import it.configuration.ErrorManager;

@Service
public class StatsService {
	
	// attributi
	private DatabaseManager databaseManager = null;
	private CheckerImpl checker = null;
	private FiltratorImpl filtrator = null;
	private SorterImpl sorter = null;
	
	// metodi
	public Object getData(Vector<String> cityList, Vector<String> requestedWeather, String sortingType,
			String periodicity, Vector<String> dateSpan)	 {		
		
		checker = new CheckerImpl();
		
		boolean periodicityIsSet = false;
		
		try {
			periodicityIsSet = checker.checkDateSpan(dateSpan);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: periodicity is not valid - or - max size for 'datespan' is 3", false);
		} catch (java.text.ParseException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: date format is not valid", false);
		}
		
		try {
			checker.checkDates();
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: defined datespan is a future period of time", false);
		} catch (java.text.ParseException e2) {
			return new ErrorManager(e2, "Internal Error", true);
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "No data available before 2021", false);
		}
		
		try {
			checker.checkRequestedWeather(requestedWeather);
			if (!periodicityIsSet)
				checker.checkPeriodicity(periodicity);
			
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'weather' is 4", false);
		} catch (NumberFormatException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: '"+periodicity+"' is not a valid periodicity", false);
		}
		
		try {
			checker.checkCityList(cityList);
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'cities' is 50", false);
		} catch (DataNotFoundException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: city names are not valid", false);
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
		
		JSONArray jsonData = databaseManager.getLoadedData();
		filtrator = (FiltratorImpl)(Operator) checker; // eventualmente usare close() di Cloneable
		
		try {
			filtrator.filterByDateSpan(jsonData);
			filtrator.filterByCityList(jsonData);
			filtrator.filterByPeriodicity(jsonData);
			filtrator.filterByWeather(jsonData);
		} catch (java.text.ParseException e1) {
			return new ErrorManager(e1, "", true);
		} catch (Exception e2) {
			return new ErrorManager(e2, "", true);
		}
		
		sorter = new SorterImpl();
		
		// continua
		sorter.sort(jsonData);
		
		
		
		return jsonData;
		
		// return new ErrorManager(new Exception(), "Funziona", false);
	}
	
}
