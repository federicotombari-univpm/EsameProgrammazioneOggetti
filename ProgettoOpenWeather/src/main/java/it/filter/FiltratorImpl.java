package it.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.configuration.Configuration;
import it.utilities.Utilities;

public class FiltratorImpl extends Operator implements Filtrator {
	
	public void filterByDateSpan(JSONArray jsonData) throws ParseException { // da riscrivere più efficiente con iterator e while
		if (filterByDateSpan) {
			// scorre l'array più esterno
			for (int i=0; i<jsonData.size(); i++) {
				
				// legge la data ("timestamp")
				Date date = this.readAndParseTimestamp(jsonData, i);
				
				// rimuove l'elemento se la data è fuori dal 'datespan'
				if (date.before(startDate)) {
					jsonData.remove(i);
					i--;
				} else {
					if (date.after(endDate))
						for (int j=i+1; j<jsonData.size(); j++)
							jsonData.remove(i);
				}
			}
		}
		startDate = this.readAndParseTimestamp(jsonData, 0);
		endDate = this.readAndParseTimestamp(jsonData, jsonData.size()-1);
	}
	
	public void filterByCityList(JSONArray jsonData) throws ParseException { // da riscrivere più efficiente con iterator e while
		if (filterByCityList) {
			
			// scorre l'array più esterno
			for (int i=0; i<jsonData.size(); i++) {
			
				// legge la lista ("list")
				JSONObject jsonDataElement = (JSONObject) jsonData.get(i);
				JSONArray jsonList = (JSONArray) jsonDataElement.get("list");
			
				// scorre l'array più interno ("list")
				for (int j=0; j<jsonList.size(); j++) {
				
					// legge il nome ("name")
					JSONObject jsonListElement = (JSONObject) jsonList.get(j);
					String name = (String) jsonListElement.get("name");
			
					// confronta con 'cityList'
					boolean found = false;
					for (int k=0; k<cityList.size() && found == false; k++) {
						if (name.equals(cityList.get(k))) {
							found = true;
						}
					}
				
					// l'elemento è rimosso se la città non è tra quelle richieste in 'cityList'
					if (!found) {
						jsonList.remove(j);
						j--;
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void filterByPeriodicity(JSONArray rawData) throws ParseException  { // // da riscrivere più efficiente con do while
		JSONArray sortedData = new JSONArray();
		
		if (!filterByPeriodicity) {
			
			// crea e aggiunge l'unico elemento di sortedData
			JSONObject sortedDataElement = new JSONObject();
			sortedDataElement.put("startDate", Configuration.getDateFormatter().format(startDate)+"_00:00:00");
			sortedDataElement.put("endDate", Configuration.getDateFormatter().format(endDate)+"_23:59:59");
			JSONArray sortedList = new JSONArray();
			sortedDataElement.put("list", sortedList);			
			sortedData.add(sortedDataElement);
			
			// per ogni città scelta crea un rispettivo oggetto e lo aggiunge
			for(int i=0; i<cityList.size(); i++) {
				JSONObject sortedListElement = new JSONObject();
				sortedListElement.put("name", cityList.get(i));
				sortedListElement.put("weatherlist", new JSONArray());
				sortedList.add(sortedListElement);
			}
				
			// scorre l'array più esterno
			for (int i=0; i<rawData.size(); i++) {
					
				// legge la lista ("list")
				JSONObject rawDataElement = (JSONObject) rawData.get(i);
				JSONArray rawList = (JSONArray) rawDataElement.get("list");
					
				// scorre l'array più interno ("list")
				for (int j=0; j<rawList.size(); j++) {
						
					JSONObject rawListElement = (JSONObject) rawList.get(j);
					String name = (String) rawListElement.get("name");
					JSONObject weather = (JSONObject) rawListElement.get("weather");
					
					for (int k=0; k<sortedList.size(); k++) {
						JSONObject sortedListElement = (JSONObject) sortedList.get(k);
						if (name.equals(sortedListElement.get("name"))) {
							JSONArray weatherList = (JSONArray) sortedListElement.get("weatherlist");
							weatherList.add(weather);
						}
						
					}
				}
			}
				
		} else {

			for (Date checkpointDate = startDate; checkpointDate.compareTo(endDate)<=0; checkpointDate = Utilities.addDaysToDate(checkpointDate, periodicityValue)) {							
				
				// crea e aggiunge un elemento a sortedData
				JSONObject sortedDataElement = new JSONObject();
				sortedDataElement.put("startDate", Configuration.getDateFormatter().format(checkpointDate)+"_00:00:00");
				sortedDataElement.put("endDate", Configuration.getDateFormatter().format(Utilities.addDaysToDate(checkpointDate, periodicityValue-1))+"_23:59:59");
				JSONArray sortedList = new JSONArray();
				sortedDataElement.put("list", sortedList);			
				sortedData.add(sortedDataElement);
				
				// per ogni città scelta crea un rispettivo oggetto e lo aggiunge
				for(int i=0; i<cityList.size(); i++) {
					JSONObject sortedListElement = new JSONObject();
					sortedListElement.put("name", cityList.get(i));
					sortedListElement.put("weatherlist", new JSONArray());
					sortedList.add(sortedListElement);
				}
				
				// scorre l'array più esterno
				for (int i=0; i<rawData.size(); i++) {
					
					// legge la data ("timestamp")
					Date date = this.readAndParseTimestamp(rawData, i);
					
					if (date.compareTo(checkpointDate)>=0 && date.compareTo(Utilities.addDaysToDate(checkpointDate, periodicityValue-1))<=0) {
						
						// legge la lista ("list")
						JSONObject rawDataElement = (JSONObject) rawData.get(i);
						JSONArray rawList = (JSONArray) rawDataElement.get("list");
							
						// scorre l'array più interno ("list")
						for (int j=0; j<rawList.size(); j++) {
								
							JSONObject rawListElement = (JSONObject) rawList.get(j);
							String name = (String) rawListElement.get("name");
							JSONObject weather = (JSONObject) rawListElement.get("weather");
							
							for (int k=0; k<sortedList.size(); k++) {
								JSONObject sortedListElement = (JSONObject) sortedList.get(k);
								if (name.equals(sortedListElement.get("name"))) {
									JSONArray weatherList = (JSONArray) sortedListElement.get("weatherlist");
									weatherList.add(weather);
								}
								
							}
						}
					}		
				}
			}	
		}
		rawData = sortedData;
	}
	
	
	@SuppressWarnings("unchecked")
	public void filterByWeather(JSONArray jsonData) { // da riscrivere più efficiente con iterator
		
		// scorre l'array più esterno
		for (int i=0; i<jsonData.size(); i++) {
			
			// legge la lista ("list")
			JSONObject jsonDataElement = (JSONObject) jsonData.get(i);
			JSONArray jsonList = (JSONArray) jsonDataElement.get("list");
				
			// scorre l'array più interno ("list")
			for (int j=0; j<jsonList.size(); j++) {
					
				JSONObject jsonListElement = (JSONObject) jsonList.get(j);
				JSONArray weatherList = (JSONArray) jsonListElement.get("weatherlist");
				
				Vector<Double> pressureList = new Vector<Double>();
				Vector<Double> humidityList = new Vector<Double>();
				Vector<Double> temperatureList = new Vector<Double>();
				Vector<Double> visibilityList = new Vector<Double>();
				
				for (int k=0; k<weatherList.size(); k++) {
					JSONObject weatherListElement = (JSONObject) weatherList.get(k);
					if (pressureRequested)
						pressureList.add((Double) weatherListElement.get("pressure"));
					if (humidityRequested)
						humidityList.add((Double) weatherListElement.get("humidity"));
					if (temperatureRequested)
						temperatureList.add((Double) weatherListElement.get("temperature"));
					if (visibilityRequested)
						visibilityList.add((Double) weatherListElement.get("visibility"));			
				}
				
				JSONObject stats = new JSONObject();
				
				if (pressureRequested) {
					stats.put("pressure", this.createStatsObject(pressureList));
				}
				if (humidityRequested) {
					stats.put("humidity", this.createStatsObject(humidityList));
				}	
				if (temperatureRequested) {
					stats.put("temperature", this.createStatsObject(temperatureList));
				}	
				if (visibilityRequested) {
					stats.put("visibility", this.createStatsObject(visibilityList));
				}
				
				jsonListElement.put("stats", stats);
				jsonListElement.remove("weatherlist");
			}
		}	
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject createStatsObject(Vector<Double> values) {
		JSONObject statsElement = new JSONObject();
		double average = Utilities.calcAverage(values);
		double variance = Utilities.calcVariance(values, average);
		statsElement.put("average", Utilities.roundDouble(average));
		statsElement.put("variance", Utilities.roundDouble(variance));
		return statsElement;
	}
}
