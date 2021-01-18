package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.utilities.DataDownloader;
import it.utilities.DatabaseManager;


@Service
public class AdminService {
	
	private String url = "https://api.openweathermap.org/data/2.5/weather?q=";
	private JSONObject rispAPI = null;
	private	String line = "";
	private int HttpsStatus = 0;
	private  String API;
	private String defaultCity;
	private String defaultDate;
	private String defaultTempUnit;
	private String databaseFileName;
	private int defaultZoom;
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


	AdminService adminService = new AdminService();
	DataDownloader dataDownloader = new DataDownloader();
	DatabaseManager databaseManager = new DatabaseManager();
	public Object chekAndChangeAPI (String API) throws ClassCastException, IOException, ParseException, DataNotFoundException {
		
		rispAPI = null;
		HttpsStatus = -1;
		JSONParser parser = new JSONParser();
		
		HttpsURLConnection openConnection = (HttpsURLConnection) new URL(url+"&appid="+API+"&units="+Configuration.getDefaultTempUnit()).openConnection();
		HttpsStatus = openConnection.getResponseCode();
		
		InputStream in = openConnection.getInputStream(); 
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
		
		int code = dataDownloader.getCode();
		if(code!=200) 
			return new ErrorManager(new InvalidParameterException(),"The insert API Key isn't valid, try with another one", false);
		else Configuration.setApiKey(API);
		return null;
		
	}
	
	public Object ceckAndChangeDefaultCity(String city) {;
		
		boolean contiene = false;
		for (int i = 0; i< Configuration.getDefaultCityList().size();i++) {
			
			if(Configuration.getDefaultCityList().get(i).equals(city)) 
				contiene = true;
			} 
			if(contiene) Configuration.setDefaultCity(city);
			else return new ErrorManager(new InvalidParameterException(),"The insert city can't be the new default city, plese try with another one", false);
			return null;
		}
	
	public Object checkAndChangeDefaultDate(String date) throws java.text.ParseException {
		
		Date newDate = dateFormatter.parse(date);
		Date defaultDate = dateFormatter.parse(Configuration.getDefaultDate());
		
		if(newDate.after(defaultDate)) Configuration.setDefaultDate(date);
		else 
			return new ErrorManager(new InvalidParameterException(),"The inser date can't become the default date, please try with another one", false);
		return null;	
	}
	
	public Object checkAndChangeDefaultTempUnit(String unit) {// per ora lo implemento ma peso possa creare problemi con la parte stats, in caso rimuovilo pure
		
		if(unit.equals("Kelvin") || unit.equals("Imperial")) {
			Configuration.setDefaultTempUnit(unit);
		}
		else  return new ErrorManager(new InvalidParameterException(),"The insert unit isn't correct, please try with another one", false);
		return null;
	}
	
	
	public Object checkAndChangeDatabaseFileManager(String name) throws IOException{
		if(name.contains(".json")) {
			Configuration.setDatabaseFilename(name);
			databaseManager.saveDatabase();
		}
		else   return new ErrorManager(new InvalidParameterException(),"The new name can't be the new database name, please try with another one", false);
		return null;
	}
	
	public Object checkAndChangeZoom(int zoom) {//non sono sicuro se 50 sia troppo o troppo poco
		if(zoom>10 && zoom<=50) {
			System.out.println("The insert zoom is now the default zoom");
			Configuration.setDefaultZoom(zoom);
		}else if(zoom<10) return new ErrorManager(new InvalidParameterException(),"The inser zoom in too small, try with another one bigger", false);
		else if(zoom>50)return new ErrorManager(new InvalidParameterException(),"The inser zoom in too big, try with another one smaller", false);
		return null;


	}

	
	public Object checkAndChangeConfig(JSONObject config) throws IOException, ParseException, DataNotFoundException, java.text.ParseException {
		
		if(config.has("apikey")) {
			API = config.getString("apikey");
			adminService.chekAndChangeAPI(API);
		}
		
		if (config.has("city")) {
			defaultCity = config.getString("city");
			adminService.ceckAndChangeDefaultCity(defaultCity);
		}
		
		if(config.has("startdate")) {
			defaultDate = config.getString("startdate");
			adminService.checkAndChangeDefaultDate(defaultDate);
		}
		
		if(config.has("unit")) {
			defaultTempUnit = config.getString("unit");
			adminService.checkAndChangeDefaultTempUnit(defaultTempUnit);
		}
		
		if(config.has("database")) {
			databaseFileName = config.getString("database");
			adminService.checkAndChangeDatabaseFileManager(databaseFileName);
		}
		
		if(config.has("zoom")) {
			defaultZoom = config.getInt("zoom");
			adminService.checkAndChangeZoom(defaultZoom);
		}
		return null;
	}
	
}