package it.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import exception.DataNotFoundException;
import exception.InvalidParameterException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import it.configuration.Configuration;




public class Database {
	
	private JSONArray database = null;
	private JSONObject elemento = null;
	private Vector<String>UrlMultyCall = new Vector<String>();
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Date date = new Date();
	
	DataDownloader DD = new DataDownloader ();
	
	public JSONArray getDatabase () {
		return database;
	}
	
	public void setDatabase (JSONArray database) {
		this.database = database;
	}
	
	public JSONObject getElemento() {
		return elemento;
	}
	
	public void setElemento (JSONObject elemento) {
		this.elemento = elemento;
	}
	
	public void insertElement(JSONObject elemento) {
		this.database.add(elemento);
	}
	
	public Date getDateAndTime () {
		formatter.format(date);
		return date;
	}
	
	public Vector<String> creaURLChiamateMultiple() {
		
		for(int i=0;i<Configuration.getDefaultCityList().size();i++) {
			String url = ("https://api.openweathermap.org/data/2.5/weather?q="+(Configuration.getDefaultCityList()).get(i)+"&appid="+Configuration.getApiKey());
			UrlMultyCall.add(url);
		}
		return UrlMultyCall;
	}
	
	public void creaElemento(String url) throws InvalidParameterException, ClassCastException, IOException, ParseException, DataNotFoundException { 
		

		for(int i = 0; i<UrlMultyCall.size();i++) {
		DD.chiamataAPI((UrlMultyCall).get(i));	
		JSONObject obj = new JSONObject();
		obj.put("data e ora",getDateAndTime());
		obj.put("nome",DD.getName(-1));
		obj.put("weather", DD.getMain(-1));
		obj.put("coords", DD.getCoordinates(-1));
		obj.put("code", DD.getCode());
		
	}
		}
	// salva il database in un file serializzabile
	
	public void salvaDatabase (String nome_Database) { 
		try {
			ObjectOutputStream file_output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(Configuration.getDatabaseFilename())));
			file_output.writeObject(this.database);
			file_output.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}


