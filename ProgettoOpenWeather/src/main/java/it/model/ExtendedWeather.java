package it.model;

/**
 * Classe che estende la classe Weather dello stesso package, ereditandone i metodi senza farne l'override. Agli attributi della classe
 * Weather a cui può accedere in quanto attributi "protected", questa classe aggiunge una stringa 'general' con una breve descrizione delle
 * condizioni generali del meteo (con i relativi getter e setter).
 * @author JoshuaSgariglia
 */
public class ExtendedWeather extends Weather {

	private String general;
	
	/**
	 * Costruttore della classe, che richiama quello della superclasse Weather passandole i primi quattro parametri. Il quinto parametro,
	 * 'general', è invece assegnato all'omonimo attributo.
	 * @param pressure il valore della pressione
	 * @param humidity il valore dell'umidità
	 * @param temperature il valore della temperatura
	 * @param visibility il valore della visibilità
	 * @param general la descrizione generale delle condizioni meteo
	 */
	public ExtendedWeather(double pressure, double humidity, double temperature, double visibility, String general) {
		super(pressure, humidity, temperature, visibility);
		this.general = general;
	}

	/**
	 * Metodo getter dell'attributo 'general'
	 * @return la descrizione generale del meteo
	 */
	public String getGeneral() {		
		return general;
	}

	/**
	 * Metodo setter dell'attributo 'general'
	 * @param general la nuova descrizione delle condizioni meteo
	 */
	public void setGeneral(String general) {
		this.general = general;
	}
	
}
