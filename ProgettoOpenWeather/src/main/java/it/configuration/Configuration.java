package it.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Configuration {
	
	private static String apiKey = "56989104be7410276956586c1fb09bf6";
	private static String defaultCity = "Ancona";
	private static String defaultStartDate = "2021-01-10";
	private static String defaultTempUnit = "metric";
	
	private static String configurationFilename = "config.json";
	private static String databaseFilename = "database.json";
	
	private static int defaultZoom = 10;
	private static int defaultPeriodicity = 10;
	private static int defaultThreadDelay = 7200; // secondi
	private static int defaultInitialThreadDelay = 0; // secondi
	
	private static Vector<String> defaultCityList = null;
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	private static int errorLogCounter = 0;
	
	
	public static void initializeConfig() throws IOException, ParseException, NullPointerException, ClassCastException {
		JSONParser parser = new JSONParser();
		BufferedReader fileReader = new BufferedReader(new FileReader(configurationFilename));
		JSONObject fileData = (JSONObject) parser.parse(fileReader);
		fileReader.close();
		
		apiKey = (String) fileData.get("apikey");
		defaultCity = (String) fileData.get("city");
		defaultStartDate = (String) fileData.get("startdate");
		databaseFilename = (String) fileData.get("database");
		defaultTempUnit = (String) fileData.get("unit");
		
		defaultZoom = (int)(long) fileData.get("zoom");
		defaultPeriodicity = (int)(long) fileData.get("periodicity");
		defaultThreadDelay = (int)(long) fileData.get("delay");
		defaultInitialThreadDelay = (int)(long) fileData.get("initialdelay");
		
		JSONArray cityList = (JSONArray) fileData.get("citylist");
		defaultCityList = new Vector<String>();
		for (int i=0; i<cityList.size(); i++)
			defaultCityList.add((String) cityList.get(i));
	}
	
	// getter e setter
	public static String getApiKey() {
		return apiKey;
	}
	
	public static void setApiKey(String apiKey) {
		Configuration.apiKey = apiKey;
	}
	
	public static String getDefaultCity() {
		return defaultCity;
	}
	
	public static void setDefaultCity(String defaultCity) {
		Configuration.defaultCity = defaultCity;
	}
	
	public static String getConfigurationFilename() {
		return configurationFilename;
	}
	
	public static void setConfigurationFilename(String configurationFilename) {
		Configuration.configurationFilename = configurationFilename;
	}
	
	public static String getDatabaseFilename() {
		return databaseFilename;
	}
	
	public static void setDatabaseFilename(String databaseFilename) {
		Configuration.databaseFilename = databaseFilename;
	}
	
	public static int getDefaultZoom() {
		return defaultZoom;
	}
	
	public static void setDefaultZoom(int defaultZoom) {
		Configuration.defaultZoom = defaultZoom;
	}
	
	public static int getDefaultPeriodicity() {
		return defaultPeriodicity;
	}

	public static void setDefaultPeriodicity(int defaultPeriodicity) {
		Configuration.defaultPeriodicity = defaultPeriodicity;
	}

	public static Vector<String> getDefaultCityList() {
		return defaultCityList;
	}

	public static void setDefaultCityList(Vector<String> defaultCityList) {
		Configuration.defaultCityList = defaultCityList;
	}

	public static String getDefaultStartDate() {
		return defaultStartDate;
	}

	public static void setDefaultStartDate(String defaultStartDate) {
		Configuration.defaultStartDate = defaultStartDate;
	}

	public static String getDefaultTempUnit() {
		return defaultTempUnit;
	}

	public static void setDefaultTempUnit(String defaultTempUnit) {
		Configuration.defaultTempUnit = defaultTempUnit;
	}

	public static int getDefaultThreadDelay() {
		return defaultThreadDelay;
	}

	public static void setDefaultThreadDelay(int defaultThreadDelay) {
		Configuration.defaultThreadDelay = defaultThreadDelay;
	}

	public static int getDefaultInitialThreadDelay() {
		return defaultInitialThreadDelay;
	}

	public static void setDefaultInitialThreadDelay(int defaultInitialThreadDelay) {
		Configuration.defaultInitialThreadDelay = defaultInitialThreadDelay;
	}

	public static int getErrorLogCounter() {
		return errorLogCounter;
	}

	public static void increaseErrorLogCounter() {
		errorLogCounter++;
	}

	public static SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}
	
	public static SimpleDateFormat getTimeFormatter() {
		return timeFormatter;
	}
	
}
