package it.filter;

import java.util.Vector;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;

/**
 * Interfaccia che include metodi astratti per il controllo dei filtri scelti dall'utente e per definire i campi della classe Operator,
 * i quali serviranno poi nel filtraggio e nell'ordinamento.
 * @author JoshuaSgariglia
 */
public interface Checker {

	public abstract void checkDateSpan(Vector<String> dateSpan) throws InvalidParameterException, java.text.ParseException;
	public abstract void checkDates() throws InvalidParameterException, java.text.ParseException, DataNotFoundException;
	public abstract void checkPeriodicity(String periodicity) throws NumberFormatException;
	public abstract void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException;
	public abstract void checkCityList(Vector<String> cityList) throws InvalidParameterException, DataNotFoundException;
	public abstract void checkSortingType(String sortingType);
	public abstract void checkWeatherSorting(String sortingType);
	public abstract void checkStatsSorting(String sortingType);
}
