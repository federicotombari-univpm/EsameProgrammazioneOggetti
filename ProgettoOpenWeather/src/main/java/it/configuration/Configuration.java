package it.configuration;

public class Configuration {

	// costruttore, attributi, metodi sono da completare e rifinire
	// altri attributi potrebbero essere un codice\password per le chiamate riservate, o una lista di codici per errori personalizzati
	
	private static String apiKey = "56989104be7410276956586c1fb09bf6";
	private static String defaultCity = "Ancona";
	private static String defaultURL = "https://api.openweathermap.org/data/2.5/weather?q="+defaultCity+"&appid="+apiKey;
	private static String configurationFilename = "config.json";
	private static String databaseFilename = "database.json";
	private static int defaultZoom = 10;
	
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
	
	public static String getDefaultURL() {
		return defaultURL;
	}
	
	public static void setDefaultURL(String defaultURL) {
		Configuration.defaultURL = defaultURL;
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
	
}
