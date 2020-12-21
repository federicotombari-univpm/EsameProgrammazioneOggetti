package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import filter.WeatherFilter;

// qui saranno presenti le chiamate per ottenere il meteo attuale in base a una o più città, una città e un margine di errore, un box
@RestController
public class WeatherController {

	@Autowired
	WeatherFilter weatherFilter;
	
	// ricordare di aggiungere i controlli per eccezioni
	
	//panoramica
	@RequestMapping(value = "/weather/info", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityWeather() {
		return new ResponseEntity<>(weatherFilter.getInfo(), HttpStatus.OK);
	}
	
	// città singola
	@RequestMapping(value = "/weather/{city}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityWeather(@PathVariable("city") String name) {
		return new ResponseEntity<>(weatherFilter.getData(/*parametri*/), HttpStatus.OK);
	}
	
	// gruppo di città
	@RequestMapping(value = "/weather/cityList", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityWeather(@RequestBody String[] cityList) {
		return new ResponseEntity<>(weatherFilter.getData(/*parametri*/), HttpStatus.OK);
	}
	
	// box a partire da città
	@RequestMapping(value = "/weather/{city}/{[errorLon,errorLat]}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityWeather(@PathVariable("city") String name,
			@PathVariable("city") double errorLon, @PathVariable("city") double errorLat) {
		return new ResponseEntity<>(weatherFilter.getData(/*parametri*/), HttpStatus.OK);
	}
	
	// box a partire da coordinate
	@RequestMapping(value = "/weather/{[minLon,minLat,maxLon,maxLat,zoom]}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityWeather(@PathVariable("minLon") double minLon,
			@PathVariable("minLat") double minLat, @PathVariable("maxLon") double maxLon,
			@PathVariable("maxLat") double maxLat, @PathVariable("zoom") double zoom) {
		return new ResponseEntity<>("Funzione da chiamare", HttpStatus.OK);
	}
}
