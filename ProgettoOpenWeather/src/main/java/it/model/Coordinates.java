package it.model;

import it.exception.InvalidParameterException;

/**
 * Classe che definisce il model di Coordinates, composto da una coppia di coordinate, latitudine e longitudine. La classe include
 * un costruttore e i metodi getter e setter. Viene utilizzata principalmente nella "parte Weather" del progetto, in particolare in
 * WeatherService (package 'service').
 * @author JoshuaSgariglia
 */
public class Coordinates {

	// attributi
	private double latitude;
	private double longitude;

	// costruttore
	/**
	 * Costruttore della classe, che prende come parametri due valori di tipo double, una latitudine e una longitudine, li controlla
	 * affinché siano entro i limiti di valore (longitudine: -180,+180; latitudine: -90,+90). Se lo sono, procede ad assegnarli agli
	 * omonimi attributi, altrimenti lancia l'eccezione InvalidParameterException (eccezione personalizzata, package 'exception').
	 * @param latitude il valore della latitudine
	 * @param longitude il valore della longitudine
	 * @throws InvalidParameterException nel caso in cui i valori non siano validi
	 */
	public Coordinates(double latitude, double longitude) throws InvalidParameterException {
		if(latitude >= -90 && latitude <=90 && longitude >= -180 && longitude <=180) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
		else throw new InvalidParameterException();
	}

	// metodi getter e setter
	/**
	 * Metodo getter dell'attributo 'latitude'.
	 * @return il valore della latitudine
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Metodo setter dell'attributo 'latitude'.
	 * @param latitude il nuovo valore della latitudine
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Metodo getter dell'attributo 'longitudine'.
	 * @return il valore della longitudine
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Metodo setter dell'attributo 'longitude'.
	 * @param longitude il nuovo valore della longitudine
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
