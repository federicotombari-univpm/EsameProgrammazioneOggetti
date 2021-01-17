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
import it.configuration.Configuration;
import it.configuration.ErrorManager;

@Service
public class StatsService {
	
	// attributi
	private DatabaseManager databaseManager = null;
	private Operator operator = null;
	
	// metodi
	public Object getData(Vector<String> cityList, Vector<String> requestedWeather, String sortingType,
			String periodicity, Vector<String> dateSpan)	 {		
		
		try {
			operator = new Operator();
		} catch (java.text.ParseException e) {
			return new ErrorManager(e, "", true);
		}
		
		CheckerImpl checker = (CheckerImpl) operator;
		
		try {
			checker.checkDateSpan(dateSpan);
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
			return new ErrorManager(e3, "No data available before " + Configuration.getDefaultStartDate(), false);
		}
		
		try {
			checker.checkPeriodicity(periodicity);
		} catch (NumberFormatException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: '"+periodicity+"' is not a valid periodicity", false);
		}
		
		try {
			checker.checkRequestedWeather(requestedWeather);
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'weather' is 4", false);
		}
		
		try {
			checker.checkCityList(cityList);
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: max size for 'cities' is 20", false);
		} catch (DataNotFoundException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: city names are not valid", false);
		}
		
		checker.checkSortingType(sortingType);
		
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
		
		FiltratorImpl filtrator =  (FiltratorImpl) operator;
		
		try {
			filtrator.filterByDateSpan(jsonData);
		} catch (java.text.ParseException e1) {
			return new ErrorManager(e1, "", true);
		} catch (DataNotFoundException e2) {
			return new ErrorManager(e2, "No data available for the chosen 'datespan'", false);
		}
		
		try {
			filtrator.filterByCityList(jsonData);
		} catch (java.text.ParseException e1) {
			return new ErrorManager(e1, "", true);
		} catch (DataNotFoundException e2) {
			return new ErrorManager(e2, "No data available for the chosen 'citylist'", false);
		}
		
		try {
			filtrator.filterByPeriodicity(jsonData);
		} catch (java.text.ParseException e) {
			return new ErrorManager(e, "", true);
		}
		
		filtrator.filterByWeather(jsonData);
		
		SorterImpl sorter = (SorterImpl) operator;
		sorter.sortData(jsonData);
		
		return jsonData;
	}
	
}
