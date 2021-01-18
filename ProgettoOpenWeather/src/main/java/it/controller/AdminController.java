package it.controller;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.configuration.Configuration;
import it.exception.DataNotFoundException;
import it.service.AdminService;

// qui saranno presenti le chiamate riservate agli admin per modificare parametri di configurazione e gestire problemi a runtime
@RestController
public class AdminController {
	
	@Autowired
	AdminService adminSarvice;
	
	AdminService adminService = new AdminService();
	private  String apiKey;
	private  String defaultCity;
	private  String defaultDate;
	private  String defaultTempUnit;
	
	private  String databaseFilename;
	
	private static int defaultZoom;
	

	@RequestMapping(value = "/changeConfingAdmin", method = RequestMethod.PUT)
	public ResponseEntity<Object> setConfing(@RequestBody Configuration configuaration) throws IOException, ParseException, DataNotFoundException, java.text.ParseException {
		JSONParser parser = new JSONParser();
		BufferedReader fileReader = new BufferedReader(new FileReader(Configuration.getConfigurationFilename()));
		JSONObject fileData = (JSONObject) parser.parse(fileReader);
		fileReader.close();
		apiKey = (String) fileData.get("apikey");
		defaultCity = (String) fileData.get("city");
		defaultDate = (String) fileData.get("datestamp");
		databaseFilename = (String) fileData.get("database");
		defaultTempUnit = (String) fileData.get("unit");
		defaultZoom = (int)(long) fileData.get("zoom");
		return new ResponseEntity<>(adminService.checkAndChangeConfig(fileData), HttpStatus.OK);
	}
	}




