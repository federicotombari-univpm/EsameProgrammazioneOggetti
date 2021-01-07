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
import it.exception.WebServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import it.configuration.Configuration;


public class DatabaseManager {
	
	private ArrayList<JSONObject> localData = null;
	private JSONArray loadedData = null;
	private Vector<String> UrlMultiCall = null;
	private boolean updating = false;
	
	
	DataDownloader DD = new DataDownloader();
	
	public void insertElement(JSONObject elemento) {
		this.localData.add(elemento);
	}
	
	public Vector<String> updateURLMultiCall() {
		UrlMultiCall = new Vector<String>();
		
		for(int i=0; i<Configuration.getDefaultCityList().size(); i++) {
			String url = ("https://api.openweathermap.org/data/2.5/weather?q="+(Configuration.getDefaultCityList()).get(i)+"&appid="+Configuration.getApiKey());
			UrlMultiCall.add(url);
		}
		
		return UrlMultiCall;
	}
	
	
	public void createElement() throws InvalidParameterException, ClassCastException, IOException, ParseException, DataNotFoundException { 
		
		Map<String,Object> mainObj = new HashMap<String,Object>();
		mainObj.put("timestamp", Utilities.getCurrentDate());
		
		ArrayList<JSONObject> array = new ArrayList<JSONObject>();

		for(int i=0; i<UrlMultiCall.size(); i++) {
			DD.chiamataAPI((UrlMultiCall).get(i));
			
			Map<String,Object> minorObj = new HashMap<String,Object>();
			minorObj.put("name",    DD.getName(-1));
			minorObj.put("weather", DD.getMain(-1, false));
			
			array.add((JSONObject) minorObj);
		}
		
		mainObj.put("list", (JSONArray) array);
		this.insertElement((JSONObject) mainObj);
	}
	
	public void saveDatabase() { 
		try {
			BufferedWriter file_output = new BufferedWriter(new FileWriter(Configuration.getDatabaseFilename(), true));
			file_output.write(((JSONArray) this.localData).toJSONString());
			file_output.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDatabase(boolean serviceRequest) throws ParseException, FileNotFoundException, IOException, WebServiceException { 
		if (serviceRequest && updating)
				throw new WebServiceException();
			
		loadedData = new JSONArray();
		JSONParser parser = new JSONParser();
		
		BufferedReader file_input = new BufferedReader(new FileReader(Configuration.getDatabaseFilename()));
		Object obj = parser.parse(file_input);
		setLoadedData((JSONArray) obj);
		
		file_input.close();
	}
	
	public void updateDatabase() throws ClassCastException, InvalidParameterException, IOException, ParseException, DataNotFoundException, WebServiceException { 
		localData = new ArrayList<JSONObject>() {
			// auto-generated
			private static final long serialVersionUID = 1L;
		};
		
		updating = true;
		this.loadDatabase(false);
		
		for(int i=0; i<loadedData.size(); i++)
			this.insertElement((JSONObject) loadedData.get(i));
		
		updating = false;
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


