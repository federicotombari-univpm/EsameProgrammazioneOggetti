package it.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.configuration.Configuration;
import it.controller.AdminController;
import it.exception.DataNotFoundException;
import it.utilities.DataDownloader;
import it.utilities.DatabaseManager;


@Service
public class AdminService {
	
	private String url = "https://api.openweathermap.org/data/2.5/weather?q=";
	private JSONObject rispAPI = null;
	private	String line = "";
	private int HttpsStatus = 0;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	AdminController adminController = new AdminController();
	DataDownloader dataDownloader = new DataDownloader();
	DatabaseManager databaseManager = new DatabaseManager();
	public void chekAndChangeAPI (String API) throws ClassCastException, IOException, ParseException, DataNotFoundException {
		
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
			System.out.println("The insert API Key isn't valid, try with another one");
		else {
			System.out.println("The insert API Key is valid");
			adminController.changeAPI(API);
		}
		
	}
	
	public void ceckAndChangeDefaultCity(String city) {;
		
		boolean contiene = false;
		for (int i = 0; i< Configuration.getDefaultCityList().size();i++) {
			if(Configuration.getDefaultCityList().get(i).equals(city)) 
				contiene = true;
			} 
			if(contiene) {
				System.out.println("The inser city is now the new default city");
				adminController.changeDefaultCity(city);
			} else System.out.println("The insert city can't be the new default city, plese try with another one");
		}
	
	public void checkAndChangeDefaultDate(String date) throws java.text.ParseException {
		
		Date newDate = dateFormatter.parse(date);
		Date defaultDate = dateFormatter.parse(Configuration.getDefaultStartDate());
		
		if(newDate.after(defaultDate)) {
			System.out.println("Default date succesfully changed");
			adminController.changeDefaultDate(date);
		}
		else System.out.println("The inser date can't become the default date, please try with another one");	
	}
	
	public void checkAndChangeDefaultTempUnit(String unit) {// per ora lo implemento ma peso possa creare problemi con la parte stats, in caso rimuovilo pure
		
		if(unit.equals("Kelvin") || unit.equals("Imperial")) {
			System.out.println("The temperature unit insert is now the new default unit");
			adminController.changeDefaultTempUnit(unit);
		}
		else System.out.println("The insert unit isn't correct, please try with another one");
	}
	
	public void checkAndChangeConfigurationFileName(String name) {
		if(name.contains(".json")) {
			System.out.println("The new name can be accept");
			adminController.changeConfigurationFileName(name);
		}
		else System.out.println("The new name can't be the new configuration file name, please try with another one");
	}
	
	public void checkAndChangeDatabaseFileManager(String name) throws IOException{
		if(name.contains(".json")) {
			System.out.println("The new name can be accept");
			adminController.changeDatabaseFileName(name);
			databaseManager.saveDatabase();
		}
		else System.out.println("The new name can't be the new database name, please try with another one");
	}
	
	public void checkAndChangeZoom(int zoom) {//non sono sicuro se 50 sia troppo o troppo poco
		if(zoom>10 && zoom<=50) {
			System.out.println("The insert zoom is now the default zoom");
			adminController.changeDefaultZoom(zoom);
		}else if(zoom<10) System.out.println("The inser zoom in too small, try with another one bigger");
		
		else if(zoom>50)System.out.println("The inser zoom in too big, try with another one smaller");

	}
	
	
}
