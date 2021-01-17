package it.filter;

import java.text.ParseException;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SorterImpl extends Operator implements Sorter {
	
	public SorterImpl() throws ParseException {
		super();	
	}

	public void sortData(JSONArray jsonData) {
		if (sortFilteredData) {
			if (sortingType_main.equals("max->min"))
				this.sortByStats(jsonData, true);
			else if (sortingType_main.equals("min->max"))
				this.sortByStats(jsonData, false);
			else if (sortingType_main.equals("a->z"))
				this.sortByCityName(jsonData, true);
			else if (sortingType_main.equals("z->a"))
				this.sortByCityName(jsonData, false);
		}
	}
	
	public void sortByStats(JSONArray jsonData, boolean descending) {
		for(int i=0; i<jsonData.size(); i++) {
			JSONObject jsonDataElement = (JSONObject) jsonData.get(i);
			JSONArray jsonList = (JSONArray) jsonDataElement.get("list");
			
			boolean swap = true;
			while (swap) {
				swap = false;
				for (int j=0; j<jsonList.size()-1; j++) {
					JSONObject jsonListElement1 = (JSONObject) jsonList.get(j);
					JSONObject weather1 = (JSONObject) jsonListElement1.get(sortingType_weather);
					JSONObject jsonListElement2 = (JSONObject) jsonList.get(j+1);
					JSONObject weather2 = (JSONObject) jsonListElement2.get(sortingType_weather);
					
					if ((double)weather1.get(sortingType_stats) > (double)weather2.get(sortingType_stats)) {
						Collections.swap(jsonList, j, j+1);
						swap = true;
					}
				
				}
			}
			
			if (descending)
				Collections.reverse(jsonList);
		}
	}

	public void sortByCityName(JSONArray jsonData, boolean alphabetical) {
		for(int i=0; i<jsonData.size(); i++) {
			JSONObject jsonDataElement = (JSONObject) jsonData.get(i);
			JSONArray jsonList = (JSONArray) jsonDataElement.get("list");
			
			boolean swap = true;
			while (swap) {
				swap = false;
				for (int j=0; j<jsonList.size()-1; j++) {
					JSONObject jsonListElement1 = (JSONObject) jsonList.get(j);
					String city1 = (String) jsonListElement1.get("name");
					JSONObject jsonListElement2 = (JSONObject) jsonList.get(j+1);
					String city2 = (String) jsonListElement2.get("name");
					
					if (city1.compareTo(city2) > 0) {
						Collections.swap(jsonList, j, j+1);
						swap = true;
					}
				
				}
			}
			
			if (!alphabetical)
				Collections.reverse(jsonList);
		}
	}
}
