package it.utilities;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.configuration.Configuration;

/**
 * Classe di utilità, che contiene metodi statici, generalmente brevi, i quali non riguardano una classe o una questione in particolare,
 * bensì sono utilizzati da diverse altre classi del progetto. I due principali sottogruppi di metodi sono quelli realtivi alle
 * operazioni matematiche, e quelli che riguardano le date (lettura, formattazione) e le operazioni con esse. I metodi che possono
 * sollevare eccezioni utilizzano dei throws.
 * @author JoshuaSgariglia
 *
 */
public class Utilities {
	
	// operazioni matematiche
	/**
	 * Metodo che, dato un valore numerico di tipo double ('value'), lo arrotonda ai millesimi.
	 * @param value il valore da arrotondare
	 * @return il valore arrotondato, in double
	 */
	public static double roundDouble(double value) {
		return ((double)Math.round(value*1000))/1000;
	}
	
	/**
	 * Metodo che, data una lista di valori di tipo Double ('list'), ne calcola la media campionaria.
	 * @param list la lista di valori di cui calcolare la media
	 * @return il valore della media
	 */
	public static double calcAverage(List<Double> list) {
		double sum = 0;
		for (double value : list)
			sum+=value;
		return Utilities.roundDouble(sum/list.size());
	}
	
	/**
	 * Metodo che, data una lista di valori di tipo Double ('list') e la loro media, ne calcola la varianza.
	 * @param list la lista di valori di cui calcolare la media
	 * @param average il valore della media
	 * @return il valore della varianza
	 */
	public static double calcVariance(List<Double> list, double average) {
		double sum = 0;
		for (double value : list)
			sum+=Math.pow(value-average, 2);
		return Utilities.roundDouble(sum/list.size());
		
	}
	
	// metodi relativi alle date (lettura, formattazione, operazioni)
	/**
	 * Metodo che ottiene la data attuale come oggetto della classe Date (in 'java.util') e lo formatta sotto forma di stringa.
	 * Il parametro 'boolean' distingue il caso in cui si voglia utilizzare 'dateFormatter' piuttosto che 'timeFormatter' (attributi
	 * della classe Configuration, nell'omonimo package).
	 * @param simple per decidere se ottenere solo la data (valore "true") o anche l'ora (valore "false")
	 * @return una stringa con le informazioni sulla data attuale
	 */
	public static String getCurrentDateToString(boolean simple) {
		if (simple)
			return Configuration.getDateFormatter().format(new Date());
		else
			return Configuration.getTimeFormatter().format(new Date());
	}
	
	/**
	 * Metodo che aggiunge a una data un numero di giorni. Utilizza le classi Date e Calendar (in 'java.util').
	 * @param date la data da incrementare
	 * @param days il numero di giorni da aggiungere alla data
	 * @return la nuova data, risultante della somma
	 */
	public static Date addDaysToDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	/**
	 * Metodo che, dato un JSONObject, ne legge il campo "timestamp" aspettandosi una stringa, e lo converte in una data utilizzando
	 * il 'dateFormatter' della classe Configuration, nell'omonimo package.
	 * @param dataElement l'elemento contenente il "timestamp" della data
	 * @return la data corrispondente alla stringa
	 * @throws ParseException se la conversione da stringa a data non va a buon fine
	 */
	public static Date readAndParseTimestamp(JSONObject dataElement) throws ParseException {
		String timestamp = (String) dataElement.get("timestamp");
		return Configuration.getDateFormatter().parse(timestamp);
	}
	
	/**
	 * Metodo che, dato un JSONArray e un indice, ottiene l'elemento (JSONObject) del JSONArray alla posizione indicata e lo passa
	 * come parametro all'altro metodo 'readAndParseTimestamp' di questa stessa classe, per ottenere una data.
	 * @param data il JSONArray di cui ottenere un elemento
	 * @param index la posizione dell'elemento da ottenere
	 * @return la data ottenuta
	 * @throws ParseException lanciata eventualmente dal metodo chiamato nel return
	 */
	public static Date readAndParseTimestamp(JSONArray data, int index) throws ParseException {
		return Utilities.readAndParseTimestamp((JSONObject) data.get(index));
	}
	
	// altri metodi
	/**
	 * Metodo che, data una lista di stringhe e una stringa, controlla se quella stringa fa già parte di quella lista. In caso
	 * negativo, la aggiunge alla lista.
	 * @param list la lista di stringhe
	 * @param string la stringa da (eventualmente) aggiungere
	 */
	public static void updateList(List<String> list, String string) {
		boolean added = false;
		for(int i=0; i<list.size() && added==false; i++)
			if(list.get(i).equals(string))
				added = true;
		if(!added)
			list.add(string);
	}
}
