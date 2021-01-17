package it.filter;

import org.json.simple.JSONArray;

public interface Sorter {
	
	public abstract void sortData(JSONArray jsonData);
	public abstract void sortByStats(JSONArray jsonData, boolean descending);
	public abstract void sortByCityName(JSONArray jsonData, boolean alphabetical);
	
}
