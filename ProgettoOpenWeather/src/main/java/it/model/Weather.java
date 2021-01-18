package it.model;

/**
 * Classe che definisce il model di Weather, composto da quattro attributi double (pressione, umidità, temperatura e visibilità) realtivi al meteo.
 * Essa include un costruttore e i metodi getter e setter. Viene utilizzata principalmente nella "parte Weather" del progetto, in particolare in
 * WeatherService (package 'service'), e nel DataDownloader (package 'utilities').
 * @author JoshuaSgariglia
 */
public class Weather {

	// attributi
	/**
	 * Attributo che indica il valore della pressione (in millibar)
	 */
	protected double pressure;
	
	/**
	 * Attributo che indica il valore dell'umidità (in percentuale)
	 */
	protected double humidity;
	
	/**
	 * Attributo che indica il valore della temperatura (si veda 'defaultTempUnit' nella classe Configuration)
	 */
	protected double temperature;
	
	/**
	 * Attributo che indica il valore della visibilità (si veda 'defaultTempUnit' nella classe Configuration)
	 */
	protected double visibility;
	
	
	// costruttore
	/**
	 * Costruttore della classe, che assegna a ogni attributo il valore dell'omonimo parametro.
	 * @param pressure il valore della pressione
	 * @param humidity il valore dell'umidità
	 * @param temperature il valore della temperatura
	 * @param visibility il valore della visibilità
	 */
	public Weather(double pressure, double humidity, double temperature, double visibility) {
		this.pressure = pressure;
		this.humidity = humidity;
		this.temperature = temperature;
		this.visibility = visibility;
	}
	
	// metodi getter e setter
	/**
	 * Metodo getter dell'attributo 'pressure'
	 * @return il valore della pressione (in millibar)
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * Metodo setter dell'attributo 'pressure'
	 * @param pressure il nuovo valore della pressione
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * Metodo getter dell'attributo 'humidity'
	 * @return il valore dell'umidità (in percentuale)
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * Metodo setter dell'attributo 'humidity'
	 * @param humidity il nuovo valore dell'umidità
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	/**
	 * Metodo getter dell'attributo 'temperature'
	 * @return il valore della temperatura (si veda 'defaultTempUnit' nella classe Configuration)
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Metodo setter dell'attributo 'temperature'
	 * @param temperature il nuovo valore della temperatura
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Metodo getter dell'attributo 'visibility'
	 * @return il valore della visibilità (si veda 'defaultTempUnit' nella classe Configuration)
	 */
	public double getVisibility() {
		return visibility;
	}

	/**
	 * Metodo setter dell'attributo 'visibility'
	 * @param visibility il nuovo valore della visibilità
	 */
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}

}
