package it.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Classe di configurazione del programma. Essa include i parametri statici di configurazione del programma e
 * un metodo per l'inizializzazione dei suddetti paramteri e un metodo per incrementare l'attributo
 * 'errorLogCounter', oltre ai classici metodi getter e setter.
 * @author JoshuaSgariglia
 */
public class Configuration {
	
	private static String apiKey = "56989104be7410276956586c1fb09bf6";
	private static String defaultCity = "Ancona";
	private static String defaultStartDate = "2021-01-10";
	private static String measurementSystem = "metric";
	
	private static String configurationFilename = "config.json";
	private static String databaseFilename = "database.json";
	
	private static int defaultZoom = 10;
	private static int defaultPeriodicity = 10;
	private static int defaultThreadDelay = 7200; // secondi
	private static int defaultInitialThreadDelay = 0; // secondi
	
	private static Vector<String> defaultCityList = null;
	
	private static final int defaultErrorWidth = 2;
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	private static int errorLogCounter = 0;
	
	/**
	 * Metodo che cerca il file JSON di configurazione, lo legge e inizializza gli attributi della classe effettuando
	 * un parsing e prendendo i valori da campi predefiniti. Il file viene cercato in base al valore di default dell'attributo
	 * 'configurationFilename', e il metodo si aspetta che esso contenga un JSONObject.
	 * @throws IOException se insorgono errori nella creazione/chiusura di un file reader
	 * @throws ParseException se il file non contiene un JSONObject
	 * @throws NullPointerException se almeno uno dei campi che si tenta di leggere non è presente nel JSONObject
	 * @throws ClassCastException se si tenta di convertire senza successo un dato di un certo tipo in un altro tipo
	 */
	public static void initializeConfig() throws IOException, ParseException, NullPointerException, ClassCastException {
		JSONParser parser = new JSONParser();
		BufferedReader fileReader = new BufferedReader(new FileReader(configurationFilename));
		JSONObject fileData = (JSONObject) parser.parse(fileReader);
		fileReader.close();
		
		apiKey = (String) fileData.get("apikey");
		defaultCity = (String) fileData.get("city");
		defaultStartDate = (String) fileData.get("startdate");
		databaseFilename = (String) fileData.get("database");
		measurementSystem = (String) fileData.get("unit");
		
		defaultZoom = (int)(long) fileData.get("zoom");
		defaultPeriodicity = (int)(long) fileData.get("periodicity");
		defaultThreadDelay = (int)(long) fileData.get("delay");
		defaultInitialThreadDelay = (int)(long) fileData.get("initialdelay");
		
		JSONArray cityList = (JSONArray) fileData.get("citylist");
		defaultCityList = new Vector<String>();
		for (int i=0; i<cityList.size(); i++)
			defaultCityList.add((String) cityList.get(i));
	}
	
	/**
	 * Metodo che ottiene uno "screenshot" dei parametri di configurazione, ossia salva i loro valori nel momento in cui
	 * viene chiamato questo metodo.
	 * @return una struttura dati con i parametri di configurazione
	 */
	public static HashMap<String, Object> getScreenshot() {
		HashMap<String, Object> screenshot = new HashMap<String, Object>();
		HashMap<String, Object> main = new HashMap<String, Object>();
		HashMap<String, Object> numeric = new HashMap<String, Object>();
		HashMap<String, Object> files = new HashMap<String, Object>();
		screenshot.put("main", main);
		main.put("apikey", apiKey);
		main.put("city", defaultCity);
		main.put("startdate", defaultStartDate);
		main.put("unit", configurationFilename);
		screenshot.put("files", files);
		files.put("datafile", databaseFilename);
		files.put("configfile", configurationFilename);
		screenshot.put("numeric", numeric);
		numeric.put("zoom", defaultZoom);
		numeric.put("periodicity", defaultPeriodicity);
		numeric.put("delay", defaultThreadDelay);
		numeric.put("initialdelay", defaultInitialThreadDelay);
		numeric.put("logcounter", errorLogCounter);
		numeric.put("errorwidth", defaultErrorWidth);
		screenshot.put("citylist", defaultCityList);
		return screenshot;
		
	}
	
	/**
	 * Metodo che incrementa il valore dell'attributo 'errorLogCounter' di questa classe, valore che inizialmente è '0'
	 */
	public static void increaseErrorLogCounter() {
		errorLogCounter++;
	}
	
	// getter e setter
	
	/**
	 * Metodo getter dell'attributo 'apiKey'
	 * @return il valore di 'apiKey'
	 */
	public static String getApiKey() {
		return apiKey;
	}
	
	/**
	 * Metodo setter dell'attributo 'apiKey'
	 * @param apiKey il nuovo valore di 'apiKey'
	 */
	public static void setApiKey(String apiKey) {
		Configuration.apiKey = apiKey;
	}
	
	/**
	 * Metodo getter dell'attributo 'defaultCity'
	 * @return il valore di 'defaultCity'
	 */
	public static String getDefaultCity() {
		return defaultCity;
	}
	
	/**
	 * Metodo setter dell'attributo 'defaultCity'
	 * @param defaultCity il nuovo valore di 'defaultCity'
	 */
	public static void setDefaultCity(String defaultCity) {
		Configuration.defaultCity = defaultCity;
	}
	
	/**
	 * Metodo getter dell'attributo 'configurationFilename'
	 * @return il valore di 'configurationFilename'
	 */
	public static String getConfigurationFilename() {
		return configurationFilename;
	}
	
	/**
	 * Metodo setter dell'attributo 'configurationFilename'
	 * @param configurationFilename il nuovo valore di 'configurationFilename'
	 */
	public static void setConfigurationFilename(String configurationFilename) {
		Configuration.configurationFilename = configurationFilename;
	}
	
	/**
	 * Metodo getter dell'attributo 'databaseFilename'
	 * @return il valore di 'databaseFilename'
	 */
	public static String getDatabaseFilename() {
		return databaseFilename;
	}
	
	/**
	 * Metodo setter dell'attributo 'databaseFilename'
	 * @param databaseFilename il nuovo valore di 'databaseFilename'
	 */
	public static void setDatabaseFilename(String databaseFilename) {
		Configuration.databaseFilename = databaseFilename;
	}
	
	/**
	 * Metodo getter dell'attributo 'configurationFilename'
	 * @return il valore di 'configurationFilename'
	 */
	public static int getDefaultZoom() {
		return defaultZoom;
	}
	
	/**
	 * Metodo setter dell'attributo 'defaultZoom'
	 * @param defaultZoom il nuovo valore di 'defaultZoom'
	 */
	public static void setDefaultZoom(int defaultZoom) {
		Configuration.defaultZoom = defaultZoom;
	}
	
	/**
	 * Metodo getter dell'attributo 'defaultPeriodicity'
	 * @return il valore di 'defaultPeriodicity'
	 */
	public static int getDefaultPeriodicity() {
		return defaultPeriodicity;
	}

	/**
	 * Metodo setter dell'attributo 'defaultPeriodicity'
	 * @param defaultPeriodicity il nuovo valore di 'defaultPeriodicity'
	 */
	public static void setDefaultPeriodicity(int defaultPeriodicity) {
		Configuration.defaultPeriodicity = defaultPeriodicity;
	}
	
	/**
	 * Metodo getter dell'attributo 'defaultCityList'
	 * @return il Vector di stringhe di 'defaultCityList'
	 */
	public static Vector<String> getDefaultCityList() {
		return defaultCityList;
	}
	
	/**
	 * Metodo setter dell'attributo 'defaultCityList'
	 * @param defaultCityList il nuovo valore di 'defaultCityList'
	 */
	public static void setDefaultCityList(Vector<String> defaultCityList) {
		Configuration.defaultCityList = defaultCityList;
	}
	
	/**
	 * Metodo getter dell'attributo 'defaultStartDate'
	 * @return il valore di 'defaultStartDate' come stringa
	 */
	public static String getDefaultStartDate() {
		return defaultStartDate;
	}
	
	/**
	 * Metodo setter dell'attributo 'defaultStartDate'
	 * @param defaultStartDate il nuovo valore di 'defaultStartDate'
	 */
	public static void setDefaultStartDate(String defaultStartDate) {
		Configuration.defaultStartDate = defaultStartDate;
	}
	
	/**
	 * Metodo getter dell'attributo 'measurementSystem'
	 * @return il sistema di misura in uso
	 */
	public static String getMeasurementSystem() {
		return measurementSystem;
	}
	
	/**
	 * Metodo setter dell'attributo 'measurementSystem'
	 * @param measurementSystem il nuovo sistema di misurazione
	 */
	public static void setMeasurementSystem(String defaultTempUnit) {
		Configuration.measurementSystem = defaultTempUnit;
	}
	
	/**
	 * Metodo getter dell'attributo 'defaultThreadDelay'
	 * @return il valore di 'defaultThreadDelay'
	 */
	public static int getDefaultThreadDelay() {
		return defaultThreadDelay;
	}
	
	/**
	 * Metodo setter dell'attributo 'defaultThreadDelay'
	 * @param defaultThreadDelay il nuovo valore di 'defaultThreadDelay'
	 */
	public static void setDefaultThreadDelay(int defaultThreadDelay) {
		Configuration.defaultThreadDelay = defaultThreadDelay;
	}
	
	/**
	 * Metodo getter dell'attributo 'defaultInitialThreadDelay'
	 * @return il valore di 'defaultInitialThreadDelay'
	 */
	public static int getDefaultInitialThreadDelay() {
		return defaultInitialThreadDelay;
	}
	
	/**
	 * Metodo setter dell'attributo 'defaultInitialThreadDelay'
	 * @param defaultInitialThreadDelay il nuovo valore di 'defaultInitialThreadDelay'
	 */
	public static void setDefaultInitialThreadDelay(int defaultInitialThreadDelay) {
		Configuration.defaultInitialThreadDelay = defaultInitialThreadDelay;
	}
	
	/**
	 * Metodo getter dell'attributo 'errorLogCounter'
	 * @return il valore di 'errorLogCounter'
	 */
	public static int getErrorLogCounter() {
		return errorLogCounter;
	}
	
	/**
	 *  Metodo getter dell'attributo 'defaultErrorWidth'
	 * @return il valore di 'defaultErrorWidth'
	 */
	public static int getDefaultErrorWidth() {
		return defaultErrorWidth;
	}

	/**
	 * Metodo getter dell'attributo 'dateFormatter'
	 * @return il 'dateFormatter' come oggetto SimpleDateFormat
	 */
	public static SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}
	
	/**
	 * Metodo getter dell'attributo 'timeFormatter'
	 * @return il 'timeFormatter' come oggetto SimpleDateFormat
	 */
	public static SimpleDateFormat getTimeFormatter() {
		return timeFormatter;
	}
	
}
