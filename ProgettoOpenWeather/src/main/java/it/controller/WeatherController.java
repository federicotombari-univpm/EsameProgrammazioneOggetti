package it.controller;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.service.WeatherService;

/**
 * Controller per ottenere le informazioni meteo in base a dei parametri: utilizza un'istanza @Autowired della classe WeatherService (package 'service')
 * e consiste di alcuni metodi (con @RequestMapping di tipo GET) che nei rispettivi 'return' chiamano metodi di 'weatherService'.
 * @author JoshuaSgariglia
 */
@RestController
public class WeatherController {

	@Autowired
	WeatherService weatherService;
	
	// città singola
	/**
	 * Metodo che, dato come parametro il nome di una città, ritorna in formato JSON le informazioni meteo su quella città
	 * e le sue coordinate chiamando un metodo della classe WeatherService (package 'service').
	 * @param name il nome della città
	 * @return le informazioni sulla città
	 */
	@RequestMapping(value = "/weather/city", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityWeather(@RequestParam(value = "city", required = false) String name) {
		return new ResponseEntity<>(weatherService.getByCityName(name), HttpStatus.OK);
	}
	
	// box a partire da città
	/**
	 * Metodo che, dato il nome di una città e delle misure di errore, utilizza un metodo della classe WeatherService per
	 * definire un area geografica ed ottenere le informazioni meteo relative alle principali città di quella zona.
	 * @param name il nome della città
	 * @param defineError i margini di errore
	 * @return una lista di città con le relative informazioni meteo e coordinate
	 */
	@RequestMapping(value = "/weather/citybox", method = RequestMethod.GET)
	public ResponseEntity<Object> getCityBoxWeather(
			@RequestParam(value = "city", required = false) String name,
			@RequestParam(value = "errordef", required = false) Vector<Double> defineError) {
		return new ResponseEntity<>(weatherService.getByCityCoords(name, defineError), HttpStatus.OK);
	}
	
	/**
	 * Metodo che, date delle coordinate geografiche, utilizza un metodo della classe WeatherService per
	 * definire un area geografica ed ottenere le informazioni meteo relative alle principali città di quella zona.
	 * @param defineBox le coordinate dell'area geografica
	 * @return una lista di città con le relative informazioni meteo e coordinate
	 */
	// box a partire da coordinate
	@RequestMapping(value = "/weather/box", method = RequestMethod.GET)
	public ResponseEntity<Object> getBoxWeather(@RequestParam(value = "boxdef") Vector<Double> defineBox) {
		return new ResponseEntity<>(weatherService.getByBoxCoords(defineBox), HttpStatus.OK);
	}
}
