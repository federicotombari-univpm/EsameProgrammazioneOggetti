package it.model;

import it.configuration.Configuration;
import it.exception.InvalidParameterException;

/**
 * Classe che definisce il model di Box, composto da due attributi Coordinates (ognuna costituisce una coppia di coordinate) e uno zoom (valore intero).
 * Essa include due costruttori e i metodi getter e setter. Viene utilizzata nella "parte Weather" del progetto, in particolare in WeatherService
 * (package 'service').
 * @author JoshuaSgariglia
 */
public class Box {

	// attributi
	private Coordinates maxCoords;
	private Coordinates minCoords;
	private int zoom;
	
	// costruttori
	/**
	 * Costruttore che, dati come parametri due oggetti della classe Coordinates, li assegna agli omonimi attributi. Allo zoom è assegnato il valore
	 * di default (usando il metodo getter del parametro di configurazione 'defaultZoom').
	 * @param maxCoords le coordinate massime del Box
	 * @param minCoords le coordinate minime del Box
	 */
	public Box(Coordinates maxCoords, Coordinates minCoords) {
		this.maxCoords = maxCoords;
		this.minCoords = minCoords;
		this.zoom = Configuration.getDefaultZoom();
	}
	
	/**
	 * Metodo che, dati i valori massimi e minimi di latitudine e longitudine, e lo zoom, crea due oggetti di tipo Coordinates, uno per i valori
	 * massimi e l'altro per quelli minimi, e lo assegna agli attributi della classe. All'attributo 'zoom' è assegnato il valore assoluto del
	 * relativo parametro.
	 * @param maxLatitude la latitudine massima del Box
	 * @param maxLongitude la longitudine massima del Box
	 * @param minLatitude la latitudine minima del Box
	 * @param minLongitude la longitudine minima del Box
	 * @param zoom lo zoom del Box
	 * @throws InvalidParameterException lanciata dal costruttore della classe Coordinates
	 */
	public Box(double maxLatitude, double maxLongitude, double minLatitude, double minLongitude, int zoom) throws InvalidParameterException {
		maxCoords = new Coordinates(maxLatitude, maxLongitude);
		minCoords = new Coordinates(minLatitude, minLongitude);
		this.zoom = Math.abs(zoom);
	}

	// metodi getter e setter
	/**
	 * Metodo getter dell'attributo 'maxCoords'
	 * @return l'oggetto con le coordinate massime del Box
	 */
	public Coordinates getMaxCoords() {
		return maxCoords;
	}

	/**
	 * Metodo setter dell'attributo 'maxCoords'
	 * @param maxCoords l'oggetto con le nuove coordinate massime del Box
	 */
	public void setMaxCoords(Coordinates maxCoords) {
		this.maxCoords = maxCoords;
	}

	/**
	 * Metodo getter dell'attributo 'minCoords'
	 * @return l'oggetto con le coordinate minime del Box
	 */
	public Coordinates getMinCoords() {
		return minCoords;
	}

	/**
	 * Metodo setter dell'attributo 'minCoords'
	 * @param minCoords l'oggetto con le nuove coordinate minime del Box
	 */
	public void setMinCoords(Coordinates minCoords) {
		this.minCoords = minCoords;
	}

	/**
	 * Metodo getter dell'attributo 'zoom'
	 * @return il valore dello zoom del Box
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * Metodo setter dell'attributo 'zoom'
	 * @param zoom il nuovo valore da assegnare allo zoom del Box
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
}
