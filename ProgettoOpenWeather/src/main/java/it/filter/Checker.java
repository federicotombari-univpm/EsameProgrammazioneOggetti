package it.filter;

import java.util.Vector;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;

public interface Checker {

	public abstract boolean checkDateSpan(Vector<String> dateSpan) throws InvalidParameterException, java.text.ParseException;
	public abstract void checkDates() throws InvalidParameterException, java.text.ParseException, DataNotFoundException;
	public abstract void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException;
	public abstract void checkPeriodicity(String periodicity) throws NumberFormatException;
	public abstract void checkCityList(Vector<String> cityList) throws InvalidParameterException, DataNotFoundException;
}
