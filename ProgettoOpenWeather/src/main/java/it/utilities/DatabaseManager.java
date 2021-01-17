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
import it.model.Weather;

import it.configuration.Configuration;
import it.configuration.ErrorManager;

@SuppressWarnings("unchecked")
public class DatabaseManager{ // TimerTask implementa Runnable, l'interfaccia dei Thread
	
	private JSONArray localData = null;
	private JSONArray loadedData = null;

	private DataDownloader dataDownloader = new DataDownloader();
	
	public void createAndAddElements() throws ClassCastException, IOException, ParseException, DataNotFoundException { 
		
		JSONObject mainObj = new JSONObject();
		mainObj.put("timestamp", Utilities.getCurrentDateToString(true));
		
		JSONArray array = new JSONArray();

		for(int i=0; i<Configuration.getDefaultCityList().size(); i++) {
			String url = ("https://api.openweathermap.org/data/2.5/weather?q="+(Configuration.getDefaultCityList()).get(i));
			dataDownloader.chiamataAPI(url);
			
			JSONObject minorObj = new JSONObject();
			minorObj.put("name", Configuration.getDefaultCityList().get(i));
			
			Weather weatherObject = dataDownloader.getMain(-1, false);
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
	
	public void insertElement(JSONObject element) {
		this.localData.add(element);
	}
	
	public void saveDatabase() throws IOException { 
		BufferedWriter file_output = new BufferedWriter(new FileWriter(Configuration.getDatabaseFilename()));
		file_output.write(localData.toJSONString());
		file_output.close();
	}
	
	public void loadDatabase() throws ParseException, FileNotFoundException, IOException { 	
		loadedData = new JSONArray();
		JSONParser parser = new JSONParser();
		
		BufferedReader file_input = new BufferedReader(new FileReader(Configuration.getDatabaseFilename()));
		loadedData = (JSONArray) parser.parse(file_input);
		
		file_input.close();
	}
	
	public void copyDatabase() { 
		for(int i=0; i<loadedData.size(); i++)
			this.insertElement((JSONObject) loadedData.get(i));
	}
	
	public void updateDatabase() throws ClassCastException, IOException, ParseException, DataNotFoundException  { 
		localData = new JSONArray();
		
		try {
			try {
				this.loadDatabase();
				this.copyDatabase();
			
			} catch (FileNotFoundException e) {
				new ErrorManager(e, "System failed to find '"+Configuration.getDatabaseFilename()+"'", true);
			} finally {
				this.createAndAddElements();
				this.saveDatabase();
			}
			
		} catch (IOException e) {
			new ErrorManager(e, "An input error occurred while loading '"+Configuration.getDatabaseFilename()+"'", true);	
		} catch (ParseException e) {
			new ErrorManager(e, "Parsing error while reading from '"+Configuration.getDatabaseFilename()+"'", true);
		} 
	}

	public JSONArray getLoadedData() {
		return loadedData;
	}

	public void setLoadedData(JSONArray loadedData) {
		this.loadedData = loadedData;
	}
}


