package it.filter;

import java.text.ParseException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.exception.DataNotFoundException;

public interface Filtrator {

	public abstract void filterByDateSpan(JSONArray jsonData) throws ParseException, DataNotFoundException;
	public abstract void filterByCityList(JSONArray jsonData) throws ParseException, DataNotFoundException;
	public abstract void filterByPeriodicity(JSONArray rawData) throws ParseException;
	public abstract void filterByWeather(JSONArray jsonData);
	public abstract JSONObject createStatsObject(Vector<Double> values);
}
