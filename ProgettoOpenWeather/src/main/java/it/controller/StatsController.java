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

/**
 * Controller per ottenere le statistiche meteo in base a una serie di filtri: utilizza un'istanza @Autowired della classe StatsService (package 'service')
 * e consiste di un solo metodo (con @RequestMapping di tipo GET) che elabora le statistiche.
 * @author JoshuaSgariglia
 */
@RestController
public class StatsController {

	@Autowired
	StatsService statsService;
	
	/**
	 * Metodo per elaborare e ottenere le statistiche meteo in base a un massimo di cinque parametri (filtri) scelti dall'utente. I valori di default dei parametri,
	 * che l'utente può definire grazie ai @RequestParam, sono "none" per le stringhe e "null" per i Vector di stringhe (grazie ai "required = false");
	 * infatti, il metodo non lancia eccezioni nel caso in cui alcuni dei parametri non siano stati definiti dall'utente. Infine, il return chiama il metodo
	 * 'getData' di 'statsService', e l'oggetto ritornato sarà convertito in JSON dal ResponseEntity del framework di Spring.
	 * @param cityList lista di città di cui ottenere le statistiche
	 * @param requestedWeather tipi di informazione meteo di cui calcolare le statistiche
	 * @param sortingType modo in cui ordinare le statistiche dopo averle calcolate
	 * @param periodicity durata in giorni di ogni suddivisione temporale di 'dateSpan'
	 * @param dateSpan date di inizio e di fine del periodo di cui ottenere le statistiche
	 * @return JSONArray di statistiche meteo
	 */
	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	public ResponseEntity<Object> getStats(
				@RequestParam (value = "cities", required = false) Vector<String> cityList,
				@RequestParam (value = "weather", required = false) Vector<String> requestedWeather,
				@RequestParam (value = "sorting", defaultValue = "none") String sortingType,
				@RequestParam (value = "periodicity", defaultValue = "none") String periodicity,
				@RequestParam (value = "datespan", required = false) Vector<String> dateSpan) {
		return new ResponseEntity<>(statsService.getData(cityList, requestedWeather, sortingType, periodicity, dateSpan), HttpStatus.OK);
	}
	
}