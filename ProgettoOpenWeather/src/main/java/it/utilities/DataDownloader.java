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

public class DataDownloader {
	// sarà necessario implementare un parser per poter ottenere i dati che ci serviranno per l'archivio\database
	// possibilmente useremo una stessa classe per tutte le chiamate a OpenWeather, con parametri modificabili in base alla richiesta
	
	private JSONObject rispAPI = null;
	private	String line = "";
	private int HttpsStatus = 0;

	
	// metodo che tramite l'URL della chiamata all'API salva in una stringa tutta la risposta
	public DataDownloader() {};
	public void chiamataAPI(String url) throws IOException, ParseException, DataNotFoundException, ClassCastException {
		
		rispAPI = null;
		HttpsStatus = -1;
		JSONParser parser = new JSONParser();
		
		HttpsURLConnection openConnection = (HttpsURLConnection) new URL(url+"&appid="+Configuration.getApiKey()+"&units="+Configuration.getDefaultTempUnit()).openConnection();
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
		double temperature = (double)main.get("temp");
		  
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

	public int getCode() {
		long code = (long) rispAPI.get("cod");
		return (int)code;
	}
	
	public String getMessage() {
		String message = (String) rispAPI.get("message");
		return message;
	}
	
	public int getCnt() {
		long cnt = (long) rispAPI.get("cnt");
		return (int)cnt;
	}

	public int getHttpsStatus() {
		return HttpsStatus;
	}
	
}
