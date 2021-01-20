package it.filter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Interfaccia che include metodi astratti per l'ordinamento  in base a criteri scelti dall'utente dei dati
 * filtrati attraverso i metodi di Filtrator.
 * @author JoshuaSgariglia
 */
public interface Sorter {
	
	public abstract JSONArray sortData(JSONArray jsonData);
	public abstract JSONArray sortByStats(JSONArray jsonData, boolean descending);
	public abstract JSONArray sortByCityName(JSONArray jsonData, boolean alphabetical);
	public abstract double getStatsField(JSONObject jsonElement);
	
}
