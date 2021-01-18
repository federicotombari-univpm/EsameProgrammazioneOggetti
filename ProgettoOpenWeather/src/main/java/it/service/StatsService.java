package it.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.filter.CheckerImpl;
import it.filter.FiltratorImpl;
import it.filter.Operator;
import it.filter.SorterImpl;
import it.utilities.DatabaseManager;
import it.configuration.Configuration;
import it.configuration.ErrorManager;

/**
 * Classe @Service che di occupa di gestire le richieste che gli utenti effettuano attraverso la classe StatsController. Utilizza
 * un'istanza della classe DatabaseManager (package 'utilities') per caricare il database e ottenere le informazioni passate sulle città,
 * e un oggetto di tipo Operator (package 'filter') per controllare i filtri, filtrare i dati e infine riordinarli in base all'eventuale
 * criterio scelto dall'utente.
 * @author JoshuaSgariglia
 */
@Service
public class StatsService {
	
	private DatabaseManager databaseManager = null;
	private Operator operator = null;
	
	/**
	 * Metodo che elabora statistiche: inizialmente controlla i filtri attraverso i metodi di un oggetto della classe Checker, dunque
	 * carica i dati usando l'attributo 'databaseManager', successivamente filtra questi dati attraverso un oggetto di tipo Filtrator e
	 * infine li ordina in base alla media o alla pressione di un tipo di condizione meteo, o alfabeticamente in base al nome delle città,
	 * con un oggetto di tipo Sorter (Checker, Filtrator e Sorter sono classi del package 'filter'). Le eccezioni sono gestite tramite
	 * try-catch e la classe ErrorManager.
	 * @param cityList la lista di città di cui elaborare le statistiche
	 * @param requestedWeather le informazioni meteo oggetto delle statistiche
	 * @param sortingType il tipo di ordinamento post-elaborazione
	 * @param periodicity la durata in giorni di ogni periodo di cui calcolare statistiche
	 * @param dateSpan le date di inizio e di fine del periodo da considerare
	 * @return un JSONArray di statistiche sul meteo
	 */
	public Object getData(Vector<String> cityList, Vector<String> requestedWeather, String sortingType,
			String periodicity, Vector<String> dateSpan)	 {		
		
		try {
			operator = new Operator();
		} catch (java.text.ParseException e) {
			return new ErrorManager(e, "", true);
		}
		
		CheckerImpl checker = (CheckerImpl) operator;
		
		try {
			checker.checkDateSpan(dateSpan);
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: periodicity is not valid - or - max size for 'datespan' is 3", false);
		} catch (java.text.ParseException e2) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: date format is not valid", false);
		}
		
		try {
			checker.checkDates();
		} catch (InvalidParameterException e1) {
			return new ErrorManager(e1, "Invalid parameter: defined datespan is a future period of time", false);
		} catch (java.text.ParseException e2) {
			return new ErrorManager(e2, "Internal Error", true);
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "No data available before " + Configuration.getDefaultStartDate(), false);
		}
		
		try {
			checker.checkPeriodicity(periodicity);
		} catch (NumberFormatException e) {
			return new ErrorManager(new InvalidParameterException(), "Invalid parameter: '"+periodicity+"' is not a valid periodicity", false);
		}
		
		try {
			checker.checkRequestedWeather(requestedWeather);
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Invalid parameter: max size for 'weather' is 4", false);
		}
		
		try {
			checker.checkCityList(cityList);
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Invalid parameter: max size for 'cities' is 20", false);
		}
		
		checker.checkSortingType(sortingType);
		
		databaseManager = new DatabaseManager();
		
		try {
			databaseManager.loadDatabase();
		} catch (FileNotFoundException e1) {
			return new ErrorManager(e1, "", true);
		} catch (IOException e2) {	
			return new ErrorManager(e2, "An error occurred while loading the database", true);
		} catch (ParseException e3) {
			return new ErrorManager(e3, "", true);
		}
		
		JSONArray jsonData = databaseManager.getLoadedData();
		
		FiltratorImpl filtrator =  (FiltratorImpl) operator;
		
		try {
			filtrator.filterByDateSpan(jsonData);
		} catch (java.text.ParseException e1) {
			return new ErrorManager(e1, "", true);
		} catch (DataNotFoundException e2) {
			return new ErrorManager(e2, "No data available for the chosen 'datespan'", false);
		}
		
		try {
			filtrator.filterByCityList(jsonData);
		} catch (DataNotFoundException e) {
			return new ErrorManager(e, "No data available for the chosen 'citylist'", false);
		}
		
		try {
			filtrator.filterByPeriodicity(jsonData);
		} catch (java.text.ParseException e) {
			return new ErrorManager(e, "", true);
		}
		
		filtrator.filterByWeather(jsonData);
		
		SorterImpl sorter = (SorterImpl) operator;
		sorter.sortData(jsonData);
		
		return jsonData;
	}
	
}
