package it.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.utilities.Utilities;

/**
 * Classe che estende Operator, da cui eredita i campi che definiscono i filtri, e implementa Checker, da cui ottiene, facendone l'override,
 * i metodi per il controllo dei filtri scelti dall'utente, definendo al contempo gli attributi ereditati, i quali serviranno poi nel
 * filtraggio e nell'ordinamento dei dati (si vedano FiltratorImpl e SorterImpl). Le eccezioni sono gestite tramite throw(s).
 * @author JoshuaSgariglia
 */
public class CheckerImpl extends Operator implements Checker {
	
	/**
	 * Costruttore della classe, che richiama quello della superclasse.
	 * @throws ParseException eccezione lanciata dal costruttore della superclasse
	 */
	public CheckerImpl() throws ParseException {
		super();
	}

	/**
	 * Metodo che controlla che il parametro/filtro 'dateSpan' non sia vuoto (se lo è, modifica il valore di 'filterByDateSpan'), e
	 * successivamente modificare il valore degli attributi 'startDate' e 'endDate' in base al contenuto del parametro.
	 * @param dateSpan date che delimitano il periodo di cui si vogliono avere le statistiche 
	 * @throws InvalidParameterException se il parametro ha più di due elementi
	 * @throws java.text.ParseException in caso di errore nel parsing della stringa in data
	 */
	public void checkDateSpan(Vector<String> dateSpan) throws InvalidParameterException, java.text.ParseException {
		if(dateSpan == null) {
			filterByDateSpan = false;
		}
		
		else switch(dateSpan.size()) {
			case 2:
				startDate = Configuration.getDateFormatter().parse(dateSpan.get(0));
				endDate = Configuration.getDateFormatter().parse(dateSpan.get(1));
				break;
			case 1:
				startDate = Configuration.getDateFormatter().parse(dateSpan.get(0));
				endDate = startDate;
				break;
			default:
				throw new InvalidParameterException();
		};	
	}
	
	/**
	 * Metodo che controlla che le date siano nell'ordine temporale corretto e che non siano date future o troppo nel passato.
	 * @throws InvalidParameterException se la data di inizio del periodo è nel futuro
	 * @throws java.text.ParseException in caso di errore nel parsing di 'defaultStartDate' (vedi classe Configuration) in data
	 * @throws DataNotFoundException se la data di fine è antecedente a 'defaultStartDate' (vedi classe Configuration)
	 */
	public void checkDates() throws InvalidParameterException, java.text.ParseException, DataNotFoundException {
		if (startDate.compareTo(endDate) > 0) {
			Date aux = new Date();
			aux = startDate;
			startDate = endDate;
			endDate = aux;
		}
		
		if (startDate.after(new Date()))
			throw new InvalidParameterException();
		
		if (endDate.before(Configuration.getDateFormatter().parse(Configuration.getDefaultStartDate())))
			throw new DataNotFoundException();
	}
	
	/**
	 * Metodo che, data una stringa come parametro, controlla se questa corrisponde a dei valori predefiniti, altrimenti
	 * procede alla usa conversione della stringa in intero, modificando il valore dell'attributo 'periodicityValue'.
	 * @param periodicity la durata in giorni di ogni periodo statistico
	 * @throws NumberFormatException se il parametro non è un numero intero
	 */
	public void checkPeriodicity(String periodicity) throws NumberFormatException {
		if (periodicity.equals("daily") || startDate.equals(endDate))
			periodicityValue = 1;
		else if (periodicity.equals("weekly"))
			periodicityValue = 7;
		else if (periodicity.equals("monthly"))
			periodicityValue = 30;
		else if (periodicity.equals("none"))
			return;
		else
			periodicityValue = Math.abs(Integer.parseInt(periodicity));
	}
	
	/**
	 * Metodo che utilizza la lista di stringhe che ha come parametro per modificare eventualmente i valori degli attributi
	 * booleani relativi alle condizioni meteo. Per fare ciò, gli elementi della lista vengono confrontati con stringhe predefinite.
	 * @param requestedWeather la lista di condizioni meteo di cui si vogliono le statistiche
	 * @throws InvalidParameterException se la lista ha più di quattro elementi
	 */
	public void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException {
		if(requestedWeather == null || requestedWeather.get(0).equals("all"))
			this.setWeatherBools(true);
		
		else if(requestedWeather.size() > 4)
			throw new InvalidParameterException();
		
		else {
			boolean noFilterRequested = true;
		
			for(String weatherInfo : requestedWeather) {
				if(weatherInfo.equals("pressure") || weatherInfo.equals("prs")) {
					pressureRequested = true;
					noFilterRequested = false;
				}
				else if(weatherInfo.equals("humidity") || weatherInfo.equals("hum")) {
					humidityRequested = true;
					noFilterRequested = false;
				}
				else if(weatherInfo.equals("temperature") || weatherInfo.equals("tmp")) {
					temperatureRequested = true;
					noFilterRequested = false;
				}
				else if(weatherInfo.equals("visibility") || weatherInfo.equals("vis")) {
					visibilityRequested = true;
					noFilterRequested = false;
				}
			}
			
			if(noFilterRequested)
				this.setWeatherBools(true);
			}
	}
	
	/**
	 * Metodo che, datà una lista di nomi di città, controlla se essi corrispondono ai nomi di città di 'defaultCityList' (vedi classe Configuration).
	 * In caso affermativo, il nome della città è aggiunto all'attributo 'cityList'.
	 * @param cityList la lista dei nomi delle città
	 * @throws InvalidParameterException se la lista di nomi è troppo lunga (più di 20 elementi)
	 */
	public void checkCityList(Vector<String> cityList) throws InvalidParameterException {
		this.cityList = new Vector<String>();
		
		if (cityList == null) {
			filterByCityList = false;
			try {
				this.cityList = Configuration.getDefaultCityList();
			} catch (NullPointerException e) {
				new ErrorManager(e,"Default City List is empty", true);
				this.cityList.add(Configuration.getDefaultCity());
			}
		}
		
		else if (cityList.size() > 20) 
			throw new InvalidParameterException();
		
		else {
			Vector<String> defaultCityList = Configuration.getDefaultCityList();
			boolean found;
			
			for (String defaultCity : defaultCityList) {
				Iterator<String> iterator = cityList.iterator();
				
				found = false;
				while (iterator.hasNext() && found == false) {
					String city = iterator.next();
					if (city.equals(defaultCity)) {
						found = true;
						Utilities.updateList(this.cityList, city);
						iterator.remove();
					}
				}
			}
		}
	}
	
	/**
	 * Metodo che analizza il contenuto del parametro e stabilisce un tipo di ordinamento tra quelli predefiniti andando a modificare
	 * i valori degli attributi relativi all'ordinamento (i cui nomi sono del tipo "sortingType_xxx"). In particolare, modifica 
	 * 'sortingType_main' e in un caso chiama il metodo 'checkWeatherSorting' della stessa classe.
	 * @param sortingType il tipo di ordinamento dei dati statistici
	 */
	public void checkSortingType(String sortingType) {
		if (sortingType.length() == 9) {
			
			String mainType = sortingType.substring(0,3);
			
			if (mainType.equals("max") ||  mainType.equals("Max")) {
				sortingType_main = new String("max->min");
				this.checkWeatherSorting(sortingType);
			
			} else if (mainType.equals("min") || mainType.equals("Min")) {
				sortingType_main = new String("min->max");
				this.checkWeatherSorting(sortingType);
				
			} else {
				sortFilteredData = false;
			}	
			
		} else if (sortingType.equals("atoz") ||  sortingType.equals("AtoZ")) {
			sortingType_main = new String("a->z");
		} else if (sortingType.equals("ztoa") || sortingType.equals("ZtoA")) {
			sortingType_main = new String("z->a");
			
		} else {
			sortFilteredData = false;
		}
	}
	
	/**
	 * Metodo che analizza il contenuto del parametro e in base ad esso modifica il valore dell'attributo
	 * 'sortingType_weather'. Infine chiama il metodo 'checkStatsSorting' della stessa classe.
	 * @param sortingType il tipo di ordinamento dei dati statistici
	 */
	public void checkWeatherSorting(String sortingType) {
		String weatherType = sortingType.substring(3,6);
		
		if ((weatherType.equals("prs") || weatherType.equals("Prs")) && pressureRequested) {
			sortingType_weather = new String("pressure");
			this.checkStatsSorting(sortingType.substring(6,9));
				
		} else if ((weatherType.equals("hum") || weatherType.equals("Hum")) && humidityRequested) {
			sortingType_weather = new String("humidity");
			this.checkStatsSorting(sortingType.substring(6,9));
				
		} else if ((weatherType.equals("tmp") || weatherType.equals("Tmp")) && temperatureRequested) {
			sortingType_weather = new String("temperature");
			this.checkStatsSorting(sortingType.substring(6,9));
				
		} else if ((weatherType.equals("vis") || weatherType.equals("Vis")) && visibilityRequested) {
			sortingType_weather = new String("visibility");
			this.checkStatsSorting(sortingType.substring(6,9));
			
		} else {
			sortFilteredData = false;
		}
	}
	
	/**
	 * Metodo che analizza il contenuto del parametro e modifica in base ad esso il valore dell'attributo 'sortingType_stats'.
	 * @param statsType il tipo di ordinamento dei dati statistici per quanto riguarda le media e la varianza
	 */
	public void checkStatsSorting(String statsType) {
		if (statsType.equals("avg") || statsType.equals("Avg")) {
			sortingType_stats = new String("average");
				
		} else if (statsType.equals("var") || statsType.equals("Var")) {
			sortingType_stats = new String("variance");
			
		} else {
			sortFilteredData = false;
		}
	}
}

