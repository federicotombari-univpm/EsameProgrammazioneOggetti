package it.filter;

import java.text.ParseException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.exception.DataNotFoundException;

/**
 * Interfaccia che include metodi astratti per il filtraggio dei dati disponibili in base ai parametri scelti dall' utente, rimuovendo
 * i dati superflui e/o costruendo nuove strutture dati con le informazioni cercate.
 * @author JoshuaSgariglia
 */
public interface Filtrator {

	public abstract JSONArray filterByDateSpan(JSONArray jsonData) throws ParseException, DataNotFoundException;
	public abstract JSONArray filterByCityList(JSONArray jsonData) throws ParseException, DataNotFoundException;
	public abstract JSONArray filterByPeriodicity(JSONArray rawData) throws ParseException;
	public abstract JSONArray filterByWeather(JSONArray jsonData);
	public abstract JSONObject createStatsObject(Vector<Double> values);
}
