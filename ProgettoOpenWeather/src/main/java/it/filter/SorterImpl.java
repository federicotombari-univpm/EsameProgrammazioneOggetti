package it.filter;

import java.text.ParseException;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Classe che estende Operator, da cui eredita i campi che definiscono i filtri, e implementa Sorter, da cui ottiene, facendone l'override,
 * i metodi l'ordinamento dei dati disponbili, che avviene in base gli attributi ridefiniti precedentemente, successivamente al controllo
 * e al filtraggio dei dati (si vedano CheckerImpl e FiltratorImpl). Le eccezioni sono gestite tramite throw(s).
 * @author JoshuaSgariglia
 */
public class SorterImpl extends Operator implements Sorter {
	
	/**
	 * Costruttore della classe, che utilizza un oggetto di tipo CheckerImpl per inizializzare gli attributi.
	 * @param l'oggetto da cui ottenere i dati
	 * @throws ParseException eccezione lanciata dal costruttore della superclasse
	 */
	public SorterImpl(CheckerImpl checker) throws ParseException {
		this.sortFilteredData = checker.sortFilteredData;
		this.sortingType_main = checker.sortingType_main;
		this.sortingType_weather = checker.sortingType_weather;
		this.sortingType_stats = checker.sortingType_stats;
	}

	/**
	 * Metodo che stabilisce quale altro metodo della classe chiamare, e con quali parametri, in base al contenuto dell'attributo 'sortingType_main'.
	 * @param jsonData i dati filtrati da riordinare
	 * @return 
	 */
	public JSONArray sortData(JSONArray jsonData) {
		if (sortFilteredData) {
			if (sortingType_main.equals("max->min"))
				return this.sortByStats(jsonData, true);
			else if (sortingType_main.equals("min->max"))
				return this.sortByStats(jsonData, false);
			else if (sortingType_main.equals("a->z"))
				return this.sortByCityName(jsonData, true);
			else if (sortingType_main.equals("z->a"))
				return this.sortByCityName(jsonData, false);
		}
		return jsonData;
	}
	
	/**
	 * Metodo che scorre i dati filtrati e li ordina in base a un tipo di statistica di una certa condizione meteo, Per fare questo, si serve
	 * degli attributi 'sortingType_weather' e 'sortingType_stats' e dell'algoritmo di ordinamento BubbleSort, oltre che ad alcuni metodi
	 * statici di Collections (in 'java.util').
	 * @param jsonData i dati filtrati da riordinare
	 * @param descending per sapere se ordinare in maniera decrescente ("true") o crescente ("false")
	 */
	public JSONArray sortByStats(JSONArray jsonData, boolean descending) {
		for(int i=0; i<jsonData.size(); i++) {
			JSONObject jsonDataElement = (JSONObject) jsonData.get(i);
			JSONArray jsonList = (JSONArray) jsonDataElement.get("list");
			
			// BubbleSort
			boolean swap = true;
			while (swap) {
				swap = false;
				for (int j=0; j<jsonList.size()-1; j++) {
					if (this.getStatsField((JSONObject) jsonList.get(j)) > this.getStatsField((JSONObject) jsonList.get(j+1))) {
						Collections.swap(jsonList, j, j+1);
						swap = true;
					}
				}
			}
			
			if (descending)
				Collections.reverse(jsonList);
		}
		return jsonData;
	}

	/**
	 * Metodo che scorre i dati filtrati e li ordina alfabeticamente in base ai nomi delle città. Per fare questo, utilizza
	 * l'algoritmo di ordinamento BubbleSort, oltre che ad alcuni metodi statici di Collections (in 'java.util').
	 * @param jsonData i dati filtrati da riordinare
	 * @param alphabetical per sapere se ordinare in ordine alfabetico normale ("true") o inverso ("false")
	 */
	public JSONArray sortByCityName(JSONArray jsonData, boolean alphabetical) {
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
		return jsonData;
	}
	
	/**
	 * Metodo che dato un JSONObject, ottiene il valore di un certo tipo di statistica (in base agli attributi della classe).
	 * @param jsonElement struttura dati che contiene i dati statistici sul meteo
	 * @return il valore della statistica cercata
	 */
	public double getStatsField(JSONObject jsonElement) {
		JSONObject weatherStats = (JSONObject) jsonElement.get("weatherStats");
		JSONObject weather = (JSONObject) weatherStats.get(sortingType_weather);
		return (double) weather.get(sortingType_stats);
		
	}
}
