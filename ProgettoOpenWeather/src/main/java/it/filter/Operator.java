	package it.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import it.configuration.Configuration;
import it.utilities.Utilities;

/**
 * Classe costituita dagli attributi necessari al filtraggio e all'ordinamento dei dati per generare statistiche. La classe ha un costruttore e un metodo
 * per inizializzare gli attributi, ma questi saranno meglio definiti durante il controllo dei parametri di filtraggio scelti dall'utente (si vedano le
 * altre classi di questo package).
 * @author JoshuaSgariglia
 */
public class Operator {

	// attributi
	/**
	 * Variabile booleana che indica se è necessario filtrare in base a un arco temporale.
	 */
	protected boolean filterByDateSpan;
	
	/**
	 * Variabile booleana che indica se è necessario filtrare in base a una lista di città.
	 */
	protected boolean filterByCityList;
	
	/**
	 * Variabile booleana che indica se è necessario ordinare in base a una periodicità.
	 */
	protected boolean sortFilteredData;
	
	/**
	 * Variabile booleana che indica se sono richieste le statistiche sulla pressione.
	 */
	protected boolean pressureRequested;
	
	/**
	 * Variabile booleana che indica se sono richieste le statistiche sull'umidità.
	 */
	protected boolean humidityRequested;
	
	/**
	 * Variabile booleana che indica se sono richieste le statistiche sulla temperatura.
	 */
	protected boolean temperatureRequested;
	
	/**
	 * Variabile booleana che indica se sono richieste le statistiche sulla visibilità.
	 */
	protected boolean visibilityRequested;
	
	/**
	 * Variabile intera che indica il valore della periodicità.
	 */
	protected int periodicityValue;
	
	/**
	 * Data di inizio dell'arco temporale statistico
	 */
	protected Date startDate;
	
	/**
	 * Data di fine dell'arco temporale statistico
	 */
	protected Date endDate;
	
	/**
	 * Lista dei nomi delle città oggetto di indagine statistica
	 */
	protected Vector<String> cityList = null;
	
	/**
	 * Variabile che indica il tipo di ordinamento principale.
	 */
	protected String sortingType_main = null;
	
	/**
	 * Variabile che indica la condizione meteo in base a cui ordinare.
	 */
	protected String sortingType_weather = null;
	
	/**
	 * Variabile che indica il tipo di statistica in base a cui ordinare.
	 */
	protected String sortingType_stats = null;
	
	// costruttore
	/**
	 * Costruttore della classe, che inizializza parte degli attributi (tutti ad eccezione delle stringhe e del Vector di stringhe, che rimangono "null"),
	 * talvolta attraverso parametri di configurazione del programma (nel caso delle date e della periodicità). Per inizializzare le variabili booleane
	 * realative al meteo, chiama il metodo della classe.
	 * @throws ParseException in caso di errore nel parsing del timestamp da String a Date (classe di 'java.util')
	 */
	public Operator() throws ParseException {
		filterByDateSpan = true;
		filterByCityList = true;
		sortFilteredData = true;
		
		this.setWeatherBools(false);
		
		periodicityValue = Configuration.getDefaultPeriodicity();
		
		startDate = Configuration.getDateFormatter().parse(Configuration.getDefaultStartDate());
		endDate = Utilities.addDaysToDate(startDate, Configuration.getDefaultPeriodicity()-1);
	}
	
	
	// altri metodi
	/**
	 * Metodo che imposta le variabili booleane relative ai tipi di condizione meteo richiesti con il valore del parametro.
	 * @param value il nuovo valore degli attributi bool sulle condizioni meteo richieste
	 */
	public void setWeatherBools(boolean value) {
		pressureRequested = value;
		humidityRequested = value;
		temperatureRequested = value;
		visibilityRequested = value;
	}
	
	/**
	 * Metodo getter dell'attributo 'cityList'
	 * @return la lista di città di cui elaborare le statistiche meteo
	 */
	public Vector<String> getCityList() {
		return cityList;
	}
	
}
