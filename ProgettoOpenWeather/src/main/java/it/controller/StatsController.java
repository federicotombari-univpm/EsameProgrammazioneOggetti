package it.controller;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.service.StatsService;

// qui saranno presenti le chiamate per ottenere le statistiche calcolate in base ai dati dell'archivio
@RestController
public class StatsController {

	@Autowired
	StatsService statsService;
	
	// ricordare di aggiungere i controlli per eccezioni
	
	// statistiche
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public ResponseEntity<Object> getStats(
				@RequestParam (value = "cities", required = false) Vector<String> cityList,
				@RequestParam (value = "weather", required = false) Vector<String> requestedWeather,
				@RequestParam (value = "sorting", defaultValue = "none") String sortingType,
				@RequestParam (value = "periodicity", defaultValue = "none") String periodicity,
				@RequestParam (value = "datespan", required = false) Vector<String> dateSpan) {
		return new ResponseEntity<>(statsService.getData(cityList, requestedWeather, sortingType, periodicity, dateSpan), HttpStatus.OK);
	}
	
}