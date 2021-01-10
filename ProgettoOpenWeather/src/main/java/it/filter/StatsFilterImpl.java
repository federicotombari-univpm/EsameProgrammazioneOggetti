	package it.filter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.configuration.Configuration;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.utilities.Utilities;


public class StatsFilterImpl implements StatsFilter {

	private boolean filterByDateSpan;
	private boolean filterByCityList;
	private boolean filterByWeather;
	private boolean sortByPeriodicity;
	
	private boolean pressureRequested;
	private boolean humidityRequested;
	private boolean temperatureRequested;
	private boolean visibilityRequested;
	
	private int periodicityValue;
	
	private Vector<String> cityList;
	
	private Date startDate;
	private Date endDate;
	
	//costruttore
	public StatsFilterImpl() {
		filterByDateSpan = true;
		filterByCityList = true;
		filterByWeather = true;
		sortByPeriodicity = true;
		
		pressureRequested = false;
		humidityRequested = false;
		temperatureRequested = false;
		visibilityRequested = false;
		
		periodicityValue = 0;
		cityList = null;
		startDate = null;
		endDate = null;
	}
	
	// controlli
	public void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException {
		if(requestedWeather == null || requestedWeather.get(0).equals("all"))
			filterByWeather = false;
		
		else if(requestedWeather.size() > 4)
			throw new InvalidParameterException();
		
		else {
			boolean selectNone = true;
		
			for(String weatherInfo : requestedWeather) {
				if(weatherInfo.equals("pressure") || weatherInfo.equals("prs")) {
					pressureRequested = true;
					selectNone = false;
				}
				else if(weatherInfo.equals("humidity") || weatherInfo.equals("hum")) {
					humidityRequested = true;
					selectNone = false;
				}
				else if(weatherInfo.equals("temperature") || weatherInfo.equals("tmp")) {
					temperatureRequested = true;
					selectNone = false;
				}
				else if(weatherInfo.equals("visibility") || weatherInfo.equals("vis")) {
					visibilityRequested = true;
					selectNone = false;
				}
			}
			
			if(selectNone)
				filterByWeather = false;
			}
	}
	
	public void checkPeriodicity(String periodicity) throws NumberFormatException {
		if (periodicity.equals("none") || periodicity.equals("all-time") || startDate.equals(endDate))
			sortByPeriodicity = false;
		else if (periodicity.equals("daily"))
			periodicityValue = 1;
		else if (periodicity.equals("weekly"))
			periodicityValue = 7;
		else if (periodicity.equals("monthly"))
			periodicityValue = 30;
		else
			periodicityValue = Math.abs(Integer.parseInt(periodicity));
	}
	
	public boolean checkDateSpan(Vector<String> dateSpan) throws InvalidParameterException, java.text.ParseException {
		boolean periodicityIsSet = false;
		
		if(dateSpan == null) {
			filterByDateSpan = false;
		}
		
		else switch(dateSpan.size()) {
			case 3:
				this.checkPeriodicity(dateSpan.get(3));
				periodicityIsSet = true;
			case 2:
				startDate = Configuration.getDateFormatter().parse(dateSpan.get(0));
				endDate = Configuration.getDateFormatter().parse(dateSpan.get(1));
				break;
			case 1:
				startDate = Configuration.getDateFormatter().parse(dateSpan.get(0));
				endDate = startDate;
				break;
			default:
				throw new InvalidParameterException();
		}
		
		return periodicityIsSet;	
	}
	
	public void checkDates() throws InvalidParameterException, java.text.ParseException, DataNotFoundException {
		if (startDate.compareTo(endDate) > 0) {
			Date aux = new Date();
			aux = startDate;
			startDate = endDate;
			endDate = aux;
		}
		
		if (startDate.after(new Date()))
			throw new InvalidParameterException();
		
		if (endDate.before(Configuration.getDateFormatter().parse(Configuration.getDefaultDate())))
			throw new DataNotFoundException();
	}
	
	public void checkCityList(Vector<String> cityList) throws InvalidParameterException {
		this.cityList = new Vector<String>();
		
		if (cityList == null)
			filterByCityList = false;
		
		else if (cityList.size() > 50) 
			 throw new InvalidParameterException();
		
		else
			this.cityList = cityList;
	}
	
	// filtraggio
	public void filterByDateSpan(JSONArray jsonData) throws ParseException {
		if (filterByDateSpan) {
			// scorre l'array più esterno
			for (int i=0; i<jsonData.size(); i++) {
				
				// legge la data ("timestamp")
				Date date = Utilities.readAndParseTimestamp(jsonData, i);
				
				// rimuove l'elemento se la data è fuori dal 'datespan'
				if (date.before(startDate))
					jsonData.remove(i);
				else {
					if (date.after(endDate))
					for (int j=i; j<jsonData.size();j++)
						jsonData.remove(j);	
				}
			}
		}
		startDate = Utilities.readAndParseTimestamp(jsonData, 0);
		endDate = Utilities.readAndParseTimestamp(jsonData, jsonData.size()-1);
	}
	
	public void filterByCityList(JSONArray jsonData) throws ParseException {
		if (!filterByCityList) return;
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
				if (!found)
					jsonList.remove(i);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sortByPeriodicity(JSONArray rawData) throws ParseException {
		JSONArray sortedData = new JSONArray();
		
/*		
		if (!sortByPeriodicity) {
			if (startDate.equals(endDate)) {
				jsonDataElement.put("date", Configuration.getFormatter().format(startDate));
			} else {
				jsonDataElement.put("startDate", Configuration.getFormatter().format(startDate));
				jsonDataElement.put("endDate", Configuration.getFormatter().format(endDate));
			}
				
		} else {
*/
			for (Date checkpointDate = startDate; checkpointDate.compareTo(endDate)<=0; checkpointDate = Utilities.addDaysToDate(checkpointDate, periodicityValue)) {
				JSONObject sortedDataElement = new JSONObject();
				
				sortedDataElement.put("startDate", Configuration.getDateFormatter().format(checkpointDate));
				sortedDataElement.put("endDate", Configuration.getDateFormatter().format(Utilities.addDaysToDate(checkpointDate, periodicityValue-1)));
				
				// scorre l'array più esterno
				for (int i=0; i<rawData.size(); i++) {
					
					// legge la data ("timestamp")
					Date date = Utilities.readAndParseTimestamp(rawData, i);
					
					if (date.compareTo(checkpointDate)>=0 && date.compareTo(Utilities.addDaysToDate(checkpointDate, periodicityValue-1))<=0) {
						
					}		
				}
			}	
//		}
	}
	
	
	public void filterData(JSONArray rawData) throws ParseException {
		ArrayList<HashMap<String, Object>> filteredData = new ArrayList<HashMap<String, Object>>();
		
		// scorre l'array più esterno
		for (int i=0; i<rawData.size(); i++) {
			
			// legge la data ("timestamp")
			JSONObject rawDataElement = (JSONObject) rawData.get(i);
			String timestamp = (String) rawDataElement.get("timestamp");
			Date date = Configuration.getDateFormatter().parse(timestamp);
			
			// controlla che la data sia nel 'datespan' scelto dall'utente
			if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <=0) {
				JSONArray rawList = (JSONArray) rawDataElement.get("list");
				
				// scorre l'array più interno ("list")
				for (int j=0; j<rawList.size(); j++) {
					boolean trovato = false;
					
					// legge il nome ("name")
					JSONObject rawListElement = (JSONObject) rawList.get(j);
					String name = (String) rawListElement.get("name");
				
					// confronta con 'cityList'
					for (int k=0; k<cityList.size() && trovato == false; k++) {
						if (name.equals(cityList.get(k))) {
							trovato = true;
							
							// ottiene le condizione meteo in base a 'requestedWeather'
							JSONObject rawWeather = (JSONObject) rawListElement.get("weather");
							if (pressureRequested) {
								double pressure = (double) rawWeather.get("pressure");
								// salvare in nuova struttura dati
							}
							if (humidityRequested) {
								double humidity = (double) rawWeather.get("humidity");
								// salvare in nuova struttura dati
							}	
							if (temperatureRequested) {
								double temperature = (double) rawWeather.get("temperature");
								// salvare in nuova struttura dati
							}	
							if (visibilityRequested) {
								double visibility = (double) rawWeather.get("visibility");
								// salvare in nuova struttura dati
							}
								
							
							rawList.remove(j);
							}
						}
				}
			}		
		}
	}
	
}
