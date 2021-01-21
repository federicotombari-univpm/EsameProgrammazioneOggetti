package it.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import it.exception.DataNotFoundException;
import it.model.Weather;

import it.configuration.Configuration;
import it.configuration.ErrorManager;

/**
 * Classe per la gestione del database: essa contiene metodi per scaricare dati attraverso un suo attributo della classe DataDownloader,
 * salvarli in un file JSON, leggerli da file, copiarli e aggiornarli. I suoi due attributi di tipo JSONArray sono utilizzati da queste
 * classi per aggiornare il database periodicamente e fornire i dati quando richiesto. Le eccezioni nei metodi sono gestite tramite throws,
 * try-catch e ErrorManager.
 * @author FedericoTombari
 */
@SuppressWarnings("unchecked")
public class DatabaseManager{
	
	private JSONArray localData = null;
	private JSONArray loadedData = null;

	private DataDownloader dataDownloader = new DataDownloader();
	
	/**
	 * Metodo scarica informazioni meteo delle città di 'defaultCityList (vedi classe Configuration) e crea una struttura dati complessa.
	 * Scarica i dati con il metodo 'chiamataAPI' dell'attributo 'dataDownloader'. Legge e inserisce i dati relativi ad alcuni campi in un 
	 * JSONArray (un JSONObject per ogni città), infine posizione il JSONArray come secondo campo di un JSONObject principale (l'altro campo
	 * è un timestamp della data di quel momento).
	 * @throws ClassCastException lanciata dal metodo 'chiamataAPI' di 'dataDownloader'
	 * @throws IOException lanciata dal metodo 'chiamataAPI' di 'dataDownloader'
	 * @throws ParseException lanciata dal metodo 'chiamataAPI' di 'dataDownloader'
	 * @throws DataNotFoundException lanciata dal metodo 'chiamataAPI' di 'dataDownloader'
	 */
	public void createAndAddElements() throws ClassCastException, IOException, ParseException, DataNotFoundException { 
		
		JSONObject mainObj = new JSONObject();
		mainObj.put("timestamp", Utilities.getCurrentDateToString(true));
		
		JSONArray array = new JSONArray();

		for(int i=0; i<Configuration.getDefaultCityList().size(); i++) {
			String url = ("weather?q="+(Configuration.getDefaultCityList()).get(i));
			dataDownloader.chiamataAPI(url);
			
			JSONObject minorObj = new JSONObject();
			minorObj.put("name", Configuration.getDefaultCityList().get(i));
			
			Weather weatherObject = dataDownloader.getMain(-1, false);
			JSONObject weather = new JSONObject();
			weather.put("pressure", weatherObject.getPressure());
			weather.put("humidity", weatherObject.getHumidity());
			weather.put("temperature", weatherObject.getTemperature());
			weather.put("visibility", weatherObject.getVisibility());
			
			minorObj.put("weather", weather);
			
			array.add(minorObj);
		}
		
		mainObj.put("list", array);
		this.insertElement(mainObj);
	}
	
	/**
	 * Metodo che inserisce un elemento di tipo JSONObject in coda nell'attributo 'localData' (JSONArray); è utilizzato da altri metodi di questa
	 * classe: 'createAndAddElements' e 'copyDatabse'.
	 * @param element l'elemento da inserire nell'attributo 'localData'
	 */
	public void insertElement(JSONObject element) {
		this.localData.add(element);
	}
	
	/**
	 * Metodo che crea e apre un canale di output e salva l'attributo 'localData' in un file JSON, il cui nome è dato dal campo 'databaseFilename'
	 * di Configuration. Se il file è già presente, esso viene sovrascritto, altrimenti ne viene creato uno nuovo.
	 * @throws IOException in caso di errori nelle operazioni di output (apertura/scrittura/chiusura).
	 */
	public void saveDatabase() throws IOException { 
		BufferedWriter file_output = new BufferedWriter(new FileWriter(Configuration.getDatabaseFilename()));
		file_output.write(localData.toJSONString());
		file_output.close();
	}
	
	/**
	 * Metodo che crea e apre un canale di output e salva l'attributo 'localData' in un file JSON, il cui nome è dato dal campo 'databaseFilename'
	 * di Configuration. Se il file è già presente, esso viene sovrascritto, altrimenti ne viene creato uno nuovo.
	 * @throws FileNotFoundException in caso di fallimento della ricerca del file
	 * @throws IOException in caso di errori nelle operazioni di input (apertura/lettura/chiusura).
	 * @throws ParseException in caso di errori nel parsing del JSON
	 */
	public void loadDatabase() throws ParseException, FileNotFoundException, IOException { 	
		loadedData = new JSONArray();
		JSONParser parser = new JSONParser();
		
		BufferedReader file_input = new BufferedReader(new FileReader(Configuration.getDatabaseFilename()));
		loadedData = (JSONArray) parser.parse(file_input);
		
		file_input.close();
	}
	
	/**
	 * Metodo che inserisce uno a uno tutti gli elementi dell'attributo 'loadedData' nell'attributo 'localData', per aggiornarlo. In altri termini,
	 * aggiunge ai dati già presenti in locale quelli caricati da file (dal database).
	 */
	public void copyDatabase() { 
		for(int i=0; i<loadedData.size(); i++)
			this.insertElement((JSONObject) loadedData.get(i));
	}
	
	/**
	 * Metodo che si serve di altri metodi della classe per aggiornare il database. Per prima cosa, carica i dati contenuti nel database nell'attributo
	 * 'loadedData' e li copia nell'attributo 'localData'. Se ciò avviene senza eccezioni, scarica nuovi dati dal web server aggiunge anch'essi a
	 * 'localData', per poi sovrascriverli nel file del database. In caso di eccezioni, il metodo termina (a meno che non si tratti di una
	 * FileNotFoundException: in tal caso viene creato un nuovo file contenente solo i dati scaricati online).
	 * @throws ClassCastException lanciata dal metodo 'createAndAddElements' di questa classe
	 * @throws IOException lanciata dal metodo 'createAndAddElements' di questa classe
	 * @throws ParseException lanciata dal metodo 'createAndAddElements' di questa classe
	 * @throws DataNotFoundException lanciata dal metodo 'createAndAddElements' di questa classe
	 */
	public void updateDatabase() throws ClassCastException, IOException, ParseException, DataNotFoundException  { 
		localData = new JSONArray();
		
		try {
			try {
				this.loadDatabase();
				this.copyDatabase();
			
			} catch (FileNotFoundException e) {
				new ErrorManager(e, "System failed to find '"+Configuration.getDatabaseFilename()+"'", true);
			} finally {
				this.createAndAddElements();
				this.saveDatabase();
			}
			
		} catch (IOException e) {
			new ErrorManager(e, "An input error occurred while loading '"+Configuration.getDatabaseFilename()+"'", true);	
		} catch (ParseException e) {
			new ErrorManager(e, "Parsing error while reading from '"+Configuration.getDatabaseFilename()+"'", true);
		} 
	}

	/**
	 * Metodo getter dell'attributo 'loadedData'
	 * @return l'attributo 'loadedData' (JSONArray)
	 */
	public JSONArray getLoadedData() {
		return loadedData;
	}

	/**
	 * Metodo setter dell'attributo 'loadedData'
	 * @param loadedData il nuovo valore dell'attributo 'loadedData' (JSONArray)
	 */
	public void setLoadedData(JSONArray loadedData) {
		this.loadedData = loadedData;
	}
}


