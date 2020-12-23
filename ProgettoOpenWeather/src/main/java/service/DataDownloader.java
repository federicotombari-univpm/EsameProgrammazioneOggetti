package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataDownloader {
	// sarà necessario implementare un parser per poter ottenere i dati che ci serviranno per l'archivio\database
	// possibilmente useremo una stessa classe per tutte le chiamate a OpenWeather, con parametri modificabili in base alla richiesta
	
	String url = "https://api.openweathermap.org/data/2.5/weather?q=London&appid=64d2d3cc389d45559160f8eeb796069b";
	private JSONObject rispAPI = null;
	private	String line = "";

	
	//metodo che tramite l'URL della chiamata all'API salva in una stringa tutta la risposta
	public void chiamataAPI(String url) {
		JSONParser parser = new JSONParser();
		try {
			URLConnection openConnection = new URL(url).openConnection();
			InputStream in = openConnection.getInputStream();
		try {
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(inR);
			
			while((line = buf.readLine()) != null) {
				 rispAPI = (JSONObject) parser.parse(line); 
			}
		} finally {
			in.close();
		}
//		System.out.println("Dati scaricati: "+rispAPI);  stampa tutto il JSON di risposta
		}catch (IOException  e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	public void getMain() {
		JSONObject main = (JSONObject) rispAPI.get("main");
		  long pressure = (long)main.get("pressure");
		  long humidity = (long)main.get("humidity");
		  System.out.println("Pressure: "+pressure);
		  System.out.println("Humidity: "+humidity);
	}
	
	public void getWeather() {
		JSONArray weatherArr = (JSONArray) rispAPI.get("weather");
		JSONObject inWeather = (JSONObject)weatherArr.get(0);
		String weather = (String)inWeather.get("main");
		System.out.println("Weather: " +weather);
	}
	
	public void getCity() {
		String city = (String) rispAPI.get("name");
		System.out.println("city: "+city);
	}
	
	public void getCode() {
		long code = (long) rispAPI.get("cod");
		System.out.println("code: "+code);
	}
}
