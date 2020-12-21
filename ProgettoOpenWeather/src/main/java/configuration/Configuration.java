package configuration;

public class Configuration {

	// costruttore, attributi, metodi sono da completare e rifinire
	// altri attributi potrebbero essere un codice\password per le chiamate riservate, o una lista di codici per errori personalizzati
	
	private String apiKey;
	private String defaultFilename = "config.json";
	private int defaultZoom = 10;
	
	public Configuration(String apiKey) {
		this.apiKey = apiKey;
	}
	
}
