package it.controller;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.configuration.Configuration;
import it.service.WeatherService;

// qui saranno presenti le chiamate per ottenere il meteo attuale in base a una città, una città e un margine di errore, un box
@RestController
public class WeatherController {

	@Autowired
	WeatherService weatherService;
	
	// ricordare di aggiungere i controlli per eccezioni
	
	// città di default
	@RequestMapping(value = "/weather", method = RequestMethod.GET)
	public ResponseEntity<Object> getDefaultWeather() {
		return new ResponseEntity<>(weatherService.getByCityName(Configuration.getDefaultCity()), HttpStatus.OK);
	}
	
	// città singola
	@RequestMapping(value = "/weather/city/{city}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityWeather(@PathVariable(value = "city") String name) {
		return new ResponseEntity<>(weatherService.getByCityName(name), HttpStatus.OK);
	}
	
	// box a partire da città
	@RequestMapping(value = "/weather/citybox/{city}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityBoxWeather(@PathVariable(value = "city") String name,
			@RequestParam("errordef") Vector<Double> defineError) {
		return new ResponseEntity<>(weatherService.getByCityCoords(name, defineError), HttpStatus.OK);
	}
	
	// box a partire da coordinate
	@RequestMapping(value = "/weather/box", method = RequestMethod.GET)
	public ResponseEntity<Object> getBoxWeather(@RequestParam(value = "boxdef") Vector<Double> defineBox) {
		return new ResponseEntity<>(weatherService.getByBoxCoords(defineBox), HttpStatus.OK);
	}
}
