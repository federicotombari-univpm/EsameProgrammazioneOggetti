package it.filter;

import org.json.simple.JSONArray;

/**
 * Interfaccia che include metodi astratti per l'ordinamento  in base a criteri scelti dall'utente dei dati
 * filtrati attraverso i metodi di Filtrator.
 * @author JoshuaSgariglia
 */
public interface Sorter {
	
	public abstract void sortData(JSONArray jsonData);
	public abstract void sortByStats(JSONArray jsonData, boolean descending);
	public abstract void sortByCityName(JSONArray jsonData, boolean alphabetical);
	
}
