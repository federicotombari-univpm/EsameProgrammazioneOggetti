package it.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.configuration.Configuration;
import it.service.AdminService;

// qui saranno presenti le chiamate riservate agli admin per modificare parametri di configurazione e gestire problemi a runtime
@RestController
public class AdminController {
	
	@Autowired
	AdminService adminSarvice;

	@RequestMapping("/changeAPI")
	public void changeAPI (@RequestParam(value = "API" , defaultValue = "56989104be7410276956586c1fb09bf6") String apiKey) {
		Configuration.setApiKey(apiKey);
	}

	@RequestMapping("/changeDefaultCity")
	public void changeDefaultCity(@RequestParam(value = " New Default City" , defaultValue = "Ancona") String defaultCity) {
		Configuration.setDefaultCity(defaultCity);
	}
	
	@RequestMapping("/changeDefaultDate")
	public void changeDefaultDate(@RequestParam(value = " New Date", defaultValue = "2021-01-01") String date) {
		Configuration.setDefaultStartDate(date);
	}
		
	@RequestMapping("/changeDefaultTempUnit")
	public void changeDefaultTempUnit(@RequestParam(value = "New Temp Unit", defaultValue = "metric") String tempUnit) {
		Configuration.setDefaultTempUnit(tempUnit);
	}
	
	@RequestMapping("/changeConfigurationFileName")
	public void changeConfigurationFileName (@RequestParam(value = "New Configuration File Name", defaultValue = "config.json")String ConfigFileName) {
		Configuration.setConfigurationFilename(ConfigFileName);
	}
	
	@RequestMapping("/changeDatabaseFileName")
	public void changeDatabaseFileName (@RequestParam(value = "New Database File Name", defaultValue = "database.json")String DatabaseName) {
		Configuration.setDatabaseFilename(DatabaseName);
	}
	
	@RequestMapping("/changeDefaultZoom")
	public void changeDefaultZoom(@RequestParam(value = "New Zoom", defaultValue = "10" ) int Zoom) {
		Configuration.setDefaultZoom(Zoom);
	}
	
}


