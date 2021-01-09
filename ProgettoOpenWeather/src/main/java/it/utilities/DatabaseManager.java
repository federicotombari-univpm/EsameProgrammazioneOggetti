package it.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.model.Weather;

import java.util.Vector;

import it.configuration.Configuration;
import it.configuration.ErrorManager;

@SuppressWarnings("unchecked")
public class DatabaseManager{ // TimerTask implementa Runnable, l'interfaccia dei Thread
	
	private JSONArray localData = null;
	private JSONArray loadedData = null;
	private Vector<String> UrlMultiCall = null;

	DataDownloader DD = new DataDownloader();
	
	public Vector<String> updateURLMultiCall() {
		UrlMultiCall = new Vector<String>();
		
		for(int i=0; i<Configuration.getDefaultCityList().size(); i++) {
			String url = ("https://api.openweathermap.org/data/2.5/weather?q="+(Configuration.getDefaultCityList()).get(i)+"&appid="+Configuration.getApiKey());
			UrlMultiCall.add(url);
		}
		
		return UrlMultiCall;
	}
	
	public void createElement() throws ClassCastException, IOException, ParseException, DataNotFoundException { 
		
		JSONObject mainObj = new JSONObject();
		mainObj.put("timestamp", Utilities.getCurrentDateToString(true));
		
		JSONArray array = new JSONArray();

		for(int i=0; i<UrlMultiCall.size(); i++) {
			DD.chiamataAPI((UrlMultiCall).get(i));
			
			JSONObject minorObj = new JSONObject();
			minorObj.put("name", DD.getName(-1));
			
			Weather weatherObject = DD.getMain(-1, false);
			JSONObject weather = new JSONObject();
			weather.put("pressure", weatherObject.getPressure());
			weather.put("humidity", weatherObject.getHumidity());
			weather.put("temperature", weatherObject.getTemperature());
			weather.put("visibility", weatherObject.getVisibility());
			
			minorObj.put("weather", weather);
			
			array.add(minorObj);
		}
		
		mainObj.put("list", array);
		this.insertElement(mainObj);
	}
	
	public void insertElement(JSONObject elemento) {
		this.localData.add(elemento);
	}
	
	public void saveDatabase() throws IOException { 
		BufferedWriter file_output = new BufferedWriter(new FileWriter(Configuration.getDatabaseFilename(), false));
		file_output.write(((JSONArray) this.localData).toJSONString());	
	}
	
	public void loadDatabase() throws ParseException, FileNotFoundException, IOException { 	
		loadedData = new JSONArray();
		JSONParser parser = new JSONParser();
		
		BufferedReader file_input = new BufferedReader(new FileReader(Configuration.getDatabaseFilename()));
		Object obj = parser.parse(file_input);
		setLoadedData((JSONArray) obj);
		
		file_input.close();
	}
	
	public void updateDatabase() throws ClassCastException, IOException, ParseException, DataNotFoundException { 
		localData = new JSONArray();
		
		this.loadDatabase();
		
		for(int i=0; i<loadedData.size(); i++)
			this.insertElement((JSONObject) loadedData.get(i));
		
		this.updateURLMultiCall();
		this.createElement();
		this.saveDatabase();
	}

	public JSONArray getLoadedData() {
		return loadedData;
	}

	public void setLoadedData(JSONArray loadedData) {
		this.loadedData = loadedData;
	}
}


