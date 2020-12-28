package it.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import it.exception.DataNotFoundException;
import it.exception.InvalidParametersException;
import it.model.Coordinates;
import it.model.Weather;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataDownloader {
	// sarà necessario implementare un parser per poter ottenere i dati che ci serviranno per l'archivio\database
	// possibilmente useremo una stessa classe per tutte le chiamate a OpenWeather, con parametri modificabili in base alla richiesta
	
	private JSONObject rispAPI = null;
	private	String line = "";

	
	// metodo che tramite l'URL della chiamata all'API salva in una stringa tutta la risposta
	public void chiamataAPI(String url) throws IOException, ParseException, DataNotFoundException {
		JSONParser parser = new JSONParser();
		
			URLConnection openConnection = new URL(url).openConnection();
			InputStream in = openConnection.getInputStream();
			
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(inR);
			
			while((line = buf.readLine()) != null) {
				 rispAPI = (JSONObject) parser.parse(line); 
			}
			
			if (rispAPI == null)
				throw new DataNotFoundException();
			
			in.close();
	}
	
	public Weather getMain(int i) {
		JSONObject main = null;
		if (i == -1) {
			main = (JSONObject) rispAPI.get("main");
			
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			main = (JSONObject) object.get("main");
		}
		
		double pressure = (double)main.get("pressure");
		double humidity = (double)main.get("humidity");
		double temperature = (double)main.get("temp");
		  
		double visibility = this.getVisibility(i);
		String general = this.getGeneralWeather(i);

		Weather weather = new Weather(general, pressure, humidity, temperature, visibility);
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
		double visibility = 0;
		if (i == -1) {
			visibility = (double) rispAPI.get("visibility");
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			visibility = (double) object.get("visibility");
		}

		return visibility;
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
	
	public Coordinates getCoordinates(int i) throws InvalidParametersException {
		JSONObject coords = null;
		if (i == -1) {
			coords = (JSONObject) rispAPI.get("coords");
		} else {
			JSONArray list = (JSONArray) rispAPI.get("list");
			JSONObject object = (JSONObject) list.get(i);
			coords = (JSONObject) object.get("coords");
		}
		
		double longitude = (double) coords.get("lon");
		double latitude = (double) coords.get("lat");
		
		Coordinates cityCoords = new Coordinates(latitude, longitude);
		return cityCoords;
	}

	public int getCode() {
		int code = (int) rispAPI.get("cod");
		return code;
	}
	
	public String getMessage() {
		String message = (String) rispAPI.get("message");
		return message;
	}
	
	public int getCnt() {
		int cnt = (int) rispAPI.get("cnt");
		return cnt;
	}
	
}
