package it.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.configuration.Configuration;
import it.exception.DataNotFoundException;
import it.utilities.Utilities;

/**
 * Classe che estende Operator, da cui eredita i campi che definiscono i filtri, e implementa Filtrator, da cui ottiene, facendone l'override,
 * i metodi per il filtraggio dei dati disponibili, che avviene in base gli attributi ridefiniti precedentemente, prima di procedere
 * all'ordinamento dei dati (si vedano CheckerImpl e SorterImpl). Le eccezioni sono gestite tramite throw(s).
 * @author JoshuaSgariglia
 */
public class FiltratorImpl extends Operator implements Filtrator {
	
	/**
	 * Costruttore della classe, che richiama quello della superclasse.
	 * @throws ParseException eccezione lanciata dal costruttore della superclasse
	 */
	public FiltratorImpl() throws ParseException {
		super();
	}

	/**
	 * Metodo che (eventualmente) analizza i dati disponibili scorrendo il parametro, un JSONArray, e rimuove gli elementi che non rientrano
	 * nel periodo di tempo scelto attraverso un Iterator (classe di 'java.util'). Infine controlla che il JSONArray non sia rimasto vuoto
	 * e aggiorna le date di inizio e di fine con quelle del timestamp del primo e ultimo elemento della lista.
	 * @param jsonData i dati da filtrare secondo il periodo temporale
	 * @throws ParseException lanciata dal metodo statico 'readAndParseTimestamp' della classe Utilities, nell'omonimo package
	 * @throws DataNotFoundException se non ci sono dati disponibili per l'arco di tempo precedentemente definito
	 */
	public void filterByDateSpan(JSONArray jsonData) throws ParseException, DataNotFoundException {
		if (filterByDateSpan) {

			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = jsonData.iterator();
			
			// scorre l'array più esterno
			while (iterator.hasNext()) {
				JSONObject jsonDataElement = (JSONObject) iterator.next();

				// legge la data ("timestamp")
				Date date = Utilities.readAndParseTimestamp(jsonDataElement);
				
				// rimuove l'elemento se la data è fuori dal 'datespan'
				if (date.before(startDate) || date.after(endDate))
					iterator.remove();
			}
			
			if (jsonData.size() == 0)
				throw new DataNotFoundException();
			
			startDate = Utilities.readAndParseTimestamp(jsonData, 0);
			endDate = Utilities.readAndParseTimestamp(jsonData, jsonData.size()-1);
		}
		
	}
	
	/**
	 * Metodo che (eventualmente) analizza i dati disponibili scorrendo il parametro e leggendo per ogni elemento un'altra lista interna,
	 * e rimuove gli elementi in cui il campo "name" non è tra i nomi delle città richieste attraverso un Iterator (classe di 'java.util').
	 * Infine controlla che il JSONArray non sia rimasto vuoto. Se un'intera lista rimane vuota, l'elemento intero che contiene quella lista
	 * è rimosso. Infine controlla che siano rimasti dati con cui generare statistiche.
	 * @param jsonData i dati da filtrare secondo la lista di città
	 * @throws DataNotFoundException se non ci sono dati disponibili per i nomi di città scelti
	 */
	public void filterByCityList(JSONArray jsonData) throws DataNotFoundException {
		if (filterByCityList) {
			
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> extIterator = jsonData.iterator();
			
			// scorre l'array più esterno
			while (extIterator.hasNext()) {
				JSONObject jsonDataElement = (JSONObject) extIterator.next();
			
				// legge la lista ("list")
				JSONArray jsonList = (JSONArray) jsonDataElement.get("list");
			
				// scorre l'array più interno ("list")
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> iterator = jsonList.iterator();
				while (iterator.hasNext()) {
					JSONObject jsonListElement = (JSONObject) iterator.next();
				
					// legge il nome ("name")
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
						iterator.remove(); // rimuove jsonListElement
					}
				}
				
				if (jsonList.size() == 0)
					extIterator.remove(); // rimuove jsonDataElement
			}
			
			if (jsonData.size() == 0)
				throw new DataNotFoundException();
		}
	}
	
	/**
	 * Metodo che suddivide il periodo scelto in sottoperiodi in base al valore della periodicità, costruendo per ogni sottoperiodo una struttura
	 * dati, diversa da quella iniziale (ossia il parametro), e inserendovi contemporaneamente i dati relativi a quel periodo. Infine assegna al
	 * JSONArray iniziale quello appena costruito, in cui in ogni periodo, per ogni città, si ha una lista di condizioni meteo. Nel caso in cui
	 * l'utente non abbia precedentemente scelto una periodicità, né date di inzio e di fine, il ciclo esterno compierà una sola iterazione,
	 * inserendo tutti i dati in un unico periodo.
	 * @param rawData i dati da ordinare secondo la periodicità
	 * @throws ParseException lanciata dal metodo statico 'readAndParseTimestamp' della classe Utilities, nell'omonimo package
	 */
	@SuppressWarnings("unchecked")
	public void filterByPeriodicity(JSONArray rawData) throws ParseException  {
		JSONArray sortedData = new JSONArray();
	
		// scorre, definendoli, i sottoperiodi
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
				Date date = Utilities.readAndParseTimestamp(rawData, i);
				
				// controlla che la data della rilevazione sia compresa nel sottoperiodo
				if (date.compareTo(checkpointDate)>=0 && date.compareTo(Utilities.addDaysToDate(checkpointDate, periodicityValue-1))<=0) {
					
					// legge la lista ("list")
					JSONObject rawDataElement = (JSONObject) rawData.get(i);
					JSONArray rawList = (JSONArray) rawDataElement.get("list");
						
					// scorre l'array più interno ("list")
					for (int j=0; j<rawList.size(); j++) {
						
						// legge il nome e il meteo ("name" e "weather")
						JSONObject rawListElement = (JSONObject) rawList.get(j);
						String name = (String) rawListElement.get("name");
						JSONObject weather = (JSONObject) rawListElement.get("weather");
						
						// aggiunge "weather" nella "weatherlist" della città corrispondente, nella nuova struttura dati
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
		rawData = sortedData;
	}
	
	/**
	 * Metodo che filtra, per ogni periodo e per ogni città, i dati sul meteo in base a quelli richiesti, aggiungendoli a delle nuove liste di dati,
	 * una per ogni periodo. Chiama poi il metodo 'createStatsObject' della stessa classe per calcolare le statistiche e creare un oggetto da
	 * inserire al posto della lista di dati "weatherlist", che pertanto viene rimossa.
	 * @param jsonData i dati da filtrare secondo le condizioni meteo, e di cui calcolare le statistiche
	 */
	@SuppressWarnings("unchecked")
	public void filterByWeather(JSONArray jsonData) {
		
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
	
	/**
	 * Metodo che calcola la media e la varianza dell'elenco di valori dato come parametro. Inoltre, crea un nuovo JSONObject e vi inserisce
	 * le statistiche calcolate come campi. Utilizza, per il calcolo delle statistiche, metodi della classe Utilities, nell'omonimo package.
	 * @param values lista di valori di cui calcolare le statistiche
	 */
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
