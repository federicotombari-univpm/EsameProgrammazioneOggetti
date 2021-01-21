package it.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import it.configuration.Configuration;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;

import it.model.Coordinates;
import it.model.ExtendedWeather;
import it.model.Weather;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Classe che scarica i dati circa le informazioni meteo di città dai server di OpenWeather e permette di ottenere le informazioni che si
 * desiderano. I metodi consentono di ottenere dati per una città singola o per un elemento di una lista di città. Le eccezioni sono gestite
 * tramite throw(s).
 * @author FedericoTombari
 */
public class DataDownloader {
	
	private JSONObject rispAPI = null;
	private	String line = "";
	private int HttpsStatus = 0;

	
	// metodo che tramite l'URL della chiamata all'API salva in una stringa tutta la risposta
	/**
	 * Metodo che stabilisce una connessione con il web server utilizzando il parametro 'urlString' per definire l'URL e ne legge il codice 
	 * relativo allo stato. Quindi procede a creare un canale di input e fa il parsing di tutte le informazioni che può ottenere, salvandole
	 * in un JSONObject.
	 * @param urlString la parte di URL che definisce l'URL complessivo della chiamata
	 * @throws IOException in caso di fallimento nella connessione o nell'apertura/chiusura di un canale di input, o ancora, nella lettura del codice Https o dei dati
	 * @throws ParseException in caso di errore durante il parsing del JSONObject
	 * @throws DataNotFoundException in caso di ClassCastException durante l'operazione di parsing
	 * @throws ClassCastException in caso di insuccesso nelle conversioni di tipo durante l'apertura della connessione
	 */
	public void chiamataAPI(String urlString) throws IOException, ParseException, DataNotFoundException, ClassCastException {
		
		rispAPI = null;
		HttpsStatus = -1;
		JSONParser parser = new JSONParser();
		
		HttpsURLConnection openConnection = (HttpsURLConnection) new URL(
				"https://api.openweathermap.org/data/2.5/"+urlString+"&appid="+Configuration.getApiKey()
				+"&units="+Configuration.getMeasurementSystem()).openConnection();
		HttpsStatus = openConnection.getResponseCode();
		
		InputStream in = openConnection.getInputStream(); // qui parte l'eccezione
		InputStreamReader inR = new InputStreamReader(in);
		BufferedReader buf = new BufferedReader(inR);
			
		try {
			while((line = buf.readLine()) != null) 
				rispAPI = (JSONObject) parser.parse(line);
			
		} catch (ClassCastException e) {
			throw new DataNotFoundException();
		} finally {
			in.close();
		}
	}
		
	/**
	 * Metodo che ottiene le informazioni meteo relative a pressione, umidità, temperatura e visibilità di una città, per poi salvarle in un oggetto
	 * di tipo Weather o ExtendedWeather (package 'model'), in base al parametro 'extended'. Il parametro 'i' serve per distinguere il caso della città
	 * singola da quello del box, Se ha valore "-1" si ricade nel primo caso (e si ha un JSONObject), altrimenti indica la posizione di un elemento del
	 * JSONArray.
	 * @param i l'indice dell'elemento di cui ottenere i dati
	 * @param extended per stabilire se i dati sono del tipo ExtendedWeather o meno
	 * @return un oggetto di tipo Weather con delle informazioni meteo
	 */
	public Weather getMain(int i, boolean extended) {
		JSONObject main = null;
		if (i == -1) {
			main = (JSONObject) rispAPI.get("main");
			
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			main = (JSONObject) object.get("main");
		}
		
		long pressure = (long)main.get("pressure");
		long humidity = (long)main.get("humidity");
		
		// alcune volte temp è Double, altre Long
		long temperature;
		if (main.get("temp") instanceof Long)
			temperature = (long)main.get("temp");
		else
			temperature = (long)(double)main.get("temp");
		  
		double visibility = this.getVisibility(i);
		
		Weather weather;
		if (extended) {
			String general = this.getGeneralWeather(i);
			weather = new ExtendedWeather(pressure, humidity, temperature, visibility, general);
		} else {
			weather = new Weather(pressure, humidity, temperature, visibility);
		}
		return weather;
			
	}
	
	/**
	 * Metodo che ottiene le informazioni meteo generali di una città per ritornarle sotto forma di stringa. Il parametro 'i' serve per distinguere
	 * il caso della città singola da quello del box, Se ha valore "-1" si ricade nel primo caso (e si ha un JSONObject), altrimenti indica la
	 * posizione di un elemento del JSONArray.
	 * @param i l'indice dell'elemento di cui ottenere i dati
	 * @return una stringa con le informazioni meteo generali della città
	 */
	public String getGeneralWeather(int i) {
		JSONArray weatherArray = null;
		if (i == -1) {
			weatherArray = (JSONArray) rispAPI.get("weather");
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			weatherArray = (JSONArray) object.get("weather");
		}
		
		JSONObject inWeather = (JSONObject)weatherArray.get(0);
		String weather = (String)inWeather.get("main");
		return weather;
	}
	
	/**
	 * Metodo che ottiene le informazioni meteo relative alla visibilità di una città per ritornarle sotto forma di intero. Il parametro 'i' serve
	 * per distinguere il caso della città singola da quello del box, Se ha valore "-1" si ricade nel primo caso (e si ha un JSONObject), altrimenti
	 * indica la posizione di un elemento del JSONArray.
	 * @param i l'indice dell'elemento di cui ottenere i dati
	 * @return un valore double con la visibilità della città
	 */
	public double getVisibility(int i) {
		long visibility = 0;
		if (i == -1) {
			visibility = (long) rispAPI.get("visibility");
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			visibility = (long) object.get("visibility");
		}

		return (double)visibility;
	}
	
	/**
	 * Metodo che ottiene le informazioni relative al nome di una città per ritornarle sotto forma di stringa. Il parametro 'i' serve per 
	 * distinguere il caso della città singola da quello del box, Se ha valore "-1" si ricade nel primo caso (e si ha un JSONObject), altrimenti
	 * indica la posizione di un elemento del JSONArray.
	 * @param i l'indice dell'elemento di cui ottenere i dati
	 * @return una stringa con il nome della città
	 */
	public String getName(int i) {
		String cityName = null;
		if (i == -1) {
			cityName = (String) rispAPI.get("name");
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			cityName = (String) object.get("name");
		}
		
		return cityName;
	}
	
	/**
	 * Metodo che ottiene le coordinate di una città per ritornarle sotto forma di oggetto Coordinates (package 'model'). Il parametro 'i' serve per 
	 * distinguere il caso della città singola da quello del box, Se ha valore "-1" si ricade nel primo caso (e si ha un JSONObject), altrimenti
	 * indica la posizione di un elemento del JSONArray.
	 * @param i l'indice dell'elemento di cui ottenere i dati
	 * @return un oggetto di tipo Coordinates con le coordinate della città
	 * @throws InvalidParameterException lanciato dal costruttore della classe Coordinates
	 */
	public Coordinates getCoordinates(int i) throws InvalidParameterException {
		JSONObject coord = null;
		String lon = "lon";
		String lat = "lat";
		if (i == -1) {
			coord = (JSONObject) rispAPI.get("coord");
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			coord = (JSONObject) object.get("coord");
			lon = "Lon";
			lat = "Lat";
		}
		
		double longitude = (double) coord.get(lon);
		double latitude = (double) coord.get(lat);
		
		Coordinates cityCoords = new Coordinates(latitude, longitude);
		return cityCoords;
	}

	/**
	 * Metodo che ottiene il codice della risposta alla richiesta di connessione al web server. Può essere utilizzato per capire se la
	 * richiesta è andata a buon fine. In tal caso ci si aspetta che il valore sia "200".
	 * @return un intero con il valore del codice di risposta
	 */
	public int getCode() {
		long code = (long) rispAPI.get("cod");
		return (int)code;
	}
	
	/**
	 * Metodo che ottiene il messaggio della risposta alla richiesta di connessione al web server. Può essere utilizzato in caso di
	 * errore per capire cosa è andato storto.
	 * @return una stringa con il messaggio di errore
	 */
	public String getMessage() {
		String message = (String) rispAPI.get("message");
		return message;
	}
	
	/**
	 * Metodo che (nel caso del box) ottiene il numero di città presenti nella lista (il JSONArray con la lista è isnerito come campo
	 * del JSONObject principale).
	 * @return un intero con il valore del contatore di città rilevate
	 */
	public int getCnt() {
		long cnt = (long) rispAPI.get("cnt");
		return (int)cnt;
	}

	/**
	 * Metodo che ottiene il codice relativo allo stato Https della richiesta di connessione al web server. Può essere utilizzato per capire
	 * se la richiesta è andata a buon fine. In tal caso ci si aspetta che il valore sia "200" (lo stato relativo è "OK").
	 * @return un intero con il valore del codice relativo allo stato della richiesta
	 */
	public int getHttpsStatus() {
		return HttpsStatus;
	}
	
}
