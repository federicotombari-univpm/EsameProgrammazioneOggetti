package it.service;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.exception.WebServiceException;
import it.model.Box;
import it.model.City;
import it.model.Coordinates;
import it.utilities.DataDownloader;

/**
 * Classe @Service che di occupa di gestire le richieste che gli utenti effettuano attraverso la classe WeatherController, controllando
 * i parametri scelti e utilizzandoli per ottenere le informazioni volute. Utilizza un'istanza della classe DataDownloader per ottenere
 * le informazioni in tempo reale da un web server, e ErrorManager per gestire le eccezioni.
 * @author JoshuaSgariglia
 */
@Service
public class WeatherService {

	private DataDownloader dataDownloader = new DataDownloader();
	
	// caso "città singola"
	/**
	 * Metodo che ottiene le informazioni meteo della città 'name' utilizzando 'dataDownloader' per la richiesta al web server.
	 * Stabilisce una connessione tramite il metodo 'chiamataAPI' di 'dataDownloader, infine ritorna un oggetto di tipo City,
	 * inizializzandolo direttamente attraverso metodi di 'dataDownloader'. Tutte le eccezioni che si potrebbero verificare
	 * sono gestite attraverso try-catch e la classe ErrorManager, con l'utilizzo del metodo 'getHttpsStatus' di 'dataDownloader'.
	 * @param name il nome della città di cui ottenere il meteo
	 * @return un oggetto di tipo City con le relative informazioni meteo (a meno di eccezioni)
	 */
	public Object getByCityName(String name) { 
		
		try {
			dataDownloader.chiamataAPI("weather?q="+name);
			
		} catch (IOException e) {
			switch(dataDownloader.getHttpsStatus()) {
				case 400:
					Exception e400 = new InvalidParameterException();
					return new ErrorManager(e400, "", false);
				case 401:
					Exception e401 = new WebServiceException();
					return new ErrorManager(e401, "Requested data is unavailable, try a different request", false);
				case 404:
					Exception e404 = new DataNotFoundException();
					return new ErrorManager(e404, "Web server returned no data, city name might be invalid", false);
				case 429:
					Exception e429 = new WebServiceException();
					return new ErrorManager(e429, "Web server is overloaded, please try again later", false);
				case -1:
					return new ErrorManager(e, "", true);
				default:			
					Exception exception = new WebServiceException();
					return new ErrorManager(exception, "", false);
			}
		} catch (ParseException e2) {
			return new ErrorManager(e2, "", true);
			
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "Web server returned no data, city name might be invalid", false);
			
		} catch (Exception generalException) {
			return new ErrorManager(generalException, "", false);
		}
		
		try {
			return new City(name, dataDownloader.getCoordinates(-1), dataDownloader.getMain(-1, true));
				
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180", false);
		}	
	}

	// caso "box a partire da città"
	/**
	 * Metodo che, a partire dai parametri scelti, ottiene tramite 'dataDownloader' le coordinate della città 'name' e le utilizza,
	 * insieme al margine di errore per latitudine e longitudine definito grazie a 'defineError', per generare due coppie di
	 * coordinate con cui creare un oggetto di tipo Box da passare al metodo 'getBoxData' di questa classe, nel return.
	 * @param name il nome della città di cui ottenere le coordinate
	 * @param defineError valori di tipo Double con cui definire il margine di errore per le coordinate
	 * @return l'oggetto ritornato dal metodo 'getBoxData'
	 */
	public Object getByCityCoords(String name, List<Double> defineError) { 
		double errorLon, errorLat;
		switch(defineError.size()) {
			case 2:
				errorLat = defineError.get(0);
				errorLon = defineError.get(1);
				break;
			case 1:
				errorLat = defineError.get(0);
				errorLon = errorLat;
				break;
			case 0:
				Exception exception = new InvalidParameterException();
				return new ErrorManager(exception, "Invalid parameters: all parameters are missing", false);
			default:
				Exception exception1 = new InvalidParameterException();
				return new ErrorManager(exception1, "Invalid parameters: only 2 parameters are needed", false);
		}
		
		try {
			dataDownloader.chiamataAPI("weather?q="+name);
			
		} catch (IOException e) {
			switch(dataDownloader.getHttpsStatus()) {
				case 400:
					Exception e400 = new InvalidParameterException();
					return new ErrorManager(e400, "", false);
				case 401:
					Exception e401 = new WebServiceException();
					return new ErrorManager(e401, "Requested data is unavailable, try a different request", false);
				case 404:
					Exception e404 = new DataNotFoundException();
					return new ErrorManager(e404, "Web server returned no data, city name might be invalid", false);
				case 429:
					Exception e429 = new WebServiceException();
					return new ErrorManager(e429, "Web server is overloaded, please try again later", false);
				case -1:
					return new ErrorManager(e, "", true);
				default:			
					Exception exception = new WebServiceException();
					return new ErrorManager(exception, "", false);
			}
		} catch (ParseException e2) {
			return new ErrorManager(e2, "", true);
			
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "Web server returned no data, city name might be invalid", false);
			
		} catch (Exception generalException) {
			return new ErrorManager(generalException, "", true);
		}
		
		Box weatherBox = null;
		try {
			double latitude = dataDownloader.getCoordinates(-1).getLatitude();
			double longitude = dataDownloader.getCoordinates(-1).getLongitude();
			Coordinates maxCoords = new Coordinates(latitude+Math.abs(errorLat), longitude+Math.abs(errorLon));
			Coordinates minCoords = new Coordinates(latitude-Math.abs(errorLat), longitude-Math.abs(errorLon));
			weatherBox = new Box(maxCoords, minCoords);
			
		} catch(InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180", false);
		}
		
		return this.getBoxData(weatherBox);
	}
	
	// caso "box a partire da coordinate"
	/**
	 * Metodo che controlla i valori presenti nel parametro 'defineBox' ed eventualmente li utilizza per creare un oggetto di tipo Box.
	 * Questo oggetto viene poi passato come parametro al metodo 'getBoxData' di questa stessa classe, nel return. Le eccezioni sono
	 * gestite tramite try-catch e ErrorManager, utilizzando inoltre l'eccezione personalizzata InvalidParameterException.
	 * @param defineBox valori di tipo Double con cui definire un oggetto di tipo Box
	 * @return l'oggetto ritornato dal metodo 'getBoxData'
	 */
	public Object getByBoxCoords(List<Double> defineBox) { 
		double minLat, minLon, maxLat, maxLon, zoom;
		switch(defineBox.size()) {
			case 5:
				minLat = defineBox.get(0);
				minLon = defineBox.get(1);
				maxLat = defineBox.get(2);
				maxLon = defineBox.get(3);
				zoom   = defineBox.get(4);
				break;
			case 4:
				minLat = defineBox.get(0);
				minLon = defineBox.get(1);
				maxLat = defineBox.get(2);
				maxLon = defineBox.get(3);
				zoom   = Configuration.getDefaultZoom();
				break;
			case 3:
				minLat = defineBox.get(0);
				minLon = defineBox.get(1);
				maxLat = minLat + Configuration.getDefaultWidth();
				maxLon = minLon + Configuration.getDefaultWidth();
				zoom   = defineBox.get(2);
				break;
			case 2:
				minLat = defineBox.get(0);
				minLon = defineBox.get(1);
				maxLat = minLat + Configuration.getDefaultWidth();
				maxLon = minLon + Configuration.getDefaultWidth();
				zoom   = Configuration.getDefaultZoom();
				break;
			case 0:
				Exception exception = new InvalidParameterException();
				return new ErrorManager(exception, "Invalid parameters: all parameters are missing", false);
			default:
				Exception exception1 = new InvalidParameterException();
				return new ErrorManager(exception1, "Invalid parameters: incorrect number of parameters", false);
		}
		
		Box weatherBox = null;
		try {
			weatherBox = new Box(maxLat, maxLon, minLat, minLon, (int)zoom);
			
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180", false);
		}
		
		return this.getBoxData(weatherBox);
	}

	// caso box - parte comune
	/**
	 * Metodo che ottiene i dati meteo delle città comprese nel Box (il numero di città dipende dall'attributo 'zoom' dell'oggetto Box)
	 * attraverso 'dataDownloader'. Le informazioni meteo vengono usate per definire oggetti di tipo City, che sono inseriti in un Vector
	 * prima di farne il return. Le eccezioni sono gestite attraverso try-catch e ErrorManager, anche con l'utilizzo del metodo
	 * 'getHttpsStatus' di 'dataDownloader'.
	 * @param weatherBox l'oggetto di tipo Box utilizzato per ottenere informazioni meteo
	 * @return un Vector di City, ognuna inizializzata con le relative informazioni meteo
	 */
	public Object getBoxData(Box weatherBox) { 
		
		try {
			dataDownloader.chiamataAPI("box/city?bbox="
					+weatherBox.getMinCoords().getLatitude()+","+weatherBox.getMinCoords().getLongitude()+","
					+weatherBox.getMaxCoords().getLatitude()+","+weatherBox.getMaxCoords().getLongitude()+","
					+weatherBox.getZoom());
			
		} catch (IOException e) {
			switch(dataDownloader.getHttpsStatus()) {
				case 400:
					Exception e400 = new InvalidParameterException();
					return new ErrorManager(e400, "Invalid parameters: the requested area is too wide", false);
				case 401:
					Exception e401 = new WebServiceException();
					return new ErrorManager(e401, "Requested data is unavailable, try a different request", false);
				case 404:
					Exception e404 = new DataNotFoundException();
					if (weatherBox.getMinCoords().getLatitude() == weatherBox.getMaxCoords().getLatitude())
						return new ErrorManager(e404, "No data available: min and max latitude have the same value", false);
					else if (weatherBox.getMinCoords().getLongitude() == weatherBox.getMaxCoords().getLongitude())
						return new ErrorManager(e404, "No data available: min and max longitude have the same value", false);
					else if (weatherBox.getZoom() < 5)
						return new ErrorManager(e404, "No data available, choose a greater value for 'zoom'", false);
					else return new ErrorManager(e404, "", false);
				case 429:
					Exception e429 = new WebServiceException();
					return new ErrorManager(e429, "Web server is overloaded, please try again later", false);
				case -1:
					return new ErrorManager(e, "", true);
				default:			
					Exception exception = new WebServiceException();
					return new ErrorManager(exception, "", false);
			}
		} catch (ParseException e2) {
			return new ErrorManager(e2, "", true);
			
		} catch (DataNotFoundException e3) {
			if (weatherBox.getMinCoords().getLatitude() == weatherBox.getMaxCoords().getLatitude())
				return new ErrorManager(e3, "No data available: min and max latitude have the same value", false);
			else if (weatherBox.getMinCoords().getLongitude() == weatherBox.getMaxCoords().getLongitude())
				return new ErrorManager(e3, "No data available: min and max longitude have the same value", false);
			else if (weatherBox.getZoom() < 5)
				return new ErrorManager(e3, "No data available, choose a greater value for 'zoom'", false);
			else return new ErrorManager(e3, "", false);
			
		} catch (Exception generalException) {
			return new ErrorManager(generalException, "", true);
		}
		
		try {
			Vector<City> cityList = new Vector<City>();
			for(int i = 0; i < dataDownloader.getCnt(); i++)
				cityList.add(new City(dataDownloader.getName(i), dataDownloader.getCoordinates(i), dataDownloader.getMain(i, true)));
			return cityList; 
					
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180", false);
		}
	
	}

}
