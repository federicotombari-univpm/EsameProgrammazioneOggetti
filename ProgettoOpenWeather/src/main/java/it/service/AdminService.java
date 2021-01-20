package it.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.InvalidParameterException;
import it.utilities.DataDownloader;
import it.utilities.DatabaseManager;
import it.utilities.ThreadManager;

/**
 * Classe che gestisce le richieste arrivanti da AdminController. Utilizza try-catch e ErrorManager (package 'configuration')
 * per la gestione degli errori.
 * @author FedericoTombari
 */
@Service
public class AdminService {

	private DataDownloader dataDownloader = new DataDownloader();
	private DatabaseManager databaseManager = new DatabaseManager();
	private ThreadManager threadManager = new ThreadManager();
	private Vector<ErrorManager> errorList = new Vector<ErrorManager>();
	private String updatedParams = new String("Updated parameters: ");
	
	/**
	 * Metodo che inizializza nuovamente i parametri di configurazione servendosi di un metodo della classe Configuration,
	 * nell'omonimo package, e ottiene infine i nuovi valori dei parametri chiamando un metodo della stessa classe.
	 * @return i nuovi valori dei parametri di configurazione
	 */
	public Object reinitializeConfig() {
		try {
			Configuration.initializeConfig();
		} catch (Exception e) {
			return new ErrorManager(e, "An error occurred during re-initialization", true);
		}
		return this.getConfigStatus();
	}
	
	/**
	 * Metodo che ottiene i valori dei parametri di configurazione servendosi del metodo 'getScreenshot' della classe
	 * Controller, nell'omonimo package.
	 * @return i valori dei parametri di configurazione
	 */
	public Object getConfigStatus() {
		return Configuration.getScreenshot();
	}
	
	/**
	 * Metodo che, data una struttura dati, controlla dei campi predefiniti ed eventualmente aggiorna i valori dei relativi
	 * parametri di configurazione, aggiornando la stringa dei parametri aggiornati con successo. In caso di eccezione, salva
	 * l'errore in una lista.
	 * @param newConfig la struttura dati con possibili nuovi dati di configurazione
	 * @return l'elenco delle modifiche andate a buon fine, e una lista di eccezioni sollevate
	 */
	public Object checkAndChangeConfig(JSONObject newConfig) {
		HashMap<String,Object> hashmap = new HashMap<String,Object>();
		
		if(newConfig.containsKey("apikey"))	
			this.checkAndChangeApiKey((String) newConfig.get("apikey"));
		
		if (newConfig.containsKey("city"))
			try {
				this.checkAndChangeDefaultCity((String) newConfig.get("city"));
			} catch(NullPointerException e) {
				return new ErrorManager(e, "An error occurred while updating the default city: default citylist is null", true);
			}
		
		if(newConfig.containsKey("startdate"))
			try {
				this.checkAndChangeDefaultDate((String) newConfig.get("startdate"));
			} catch (java.text.ParseException e) {
				return new ErrorManager(e, "An error occurred while updating the default startDate", true);
			}
		
		if(newConfig.containsKey("unit"))
			this.checkAndChangeDefaultTempUnit((String) newConfig.get("unit"));
		
		if(newConfig.containsKey("database"))
			this.checkAndChangeDatabaseFileManager((String) newConfig.get("database"));
			
		if(newConfig.containsKey("zoom"))
			this.checkAndChangeZoom((int) newConfig.get("zoom"));
		
		hashmap.put("message", "No internal errors occurred");
		hashmap.put("info", updatedParams);
		hashmap.put("errorlist", errorList);
		return hashmap;
	}
	
	/**
	 * Metodo che effettua una chiamata di prova a OpenWeather per verificare la correttezza della nuova API Key. Controlla
	 * che lo stato Https sia OK prima di procedere all'aggiornamento. In tal caso, aggiunge il parametro tra quello aggiornati
	 * con successo, altrimenti aggiunge un nuovo errore alla lista.
	 * @param apiKey il nuovo valore dell'API Key
	 */
	public void checkAndChangeApiKey (String apiKey) {
		try {
			dataDownloader.chiamataAPI("weather?q="+Configuration.getDefaultCity());
			Configuration.setApiKey(apiKey);
			updatedParams = new String(updatedParams+"API Key, ");
		} catch (Exception e) {
			errorList.add(new ErrorManager(new InvalidParameterException(),"The chosen API Key isn't valid, try with another one", false));
		}
	}
		
	/**
	 * Metodo che controlla che la città immessa come parametro sia tra quelle della lista di default, e in tal caso aggiorna la città
	 * di default del programma, aggiungendo inoltre il parametro tra quello aggiornati con successo, altrimenti aggiunge un nuovo errore
	 * alla lista.
	 * @param city la nuova città di default
	 */
	public void checkAndChangeDefaultCity(String city) throws NullPointerException {
		
		boolean found = false;
		for (int i=0; i<Configuration.getDefaultCityList().size() && found == false; i++) {
			
			if(Configuration.getDefaultCityList().get(i).equals(city)) 
				found = true;
				Configuration.setDefaultCity(city);
			}
		
		 if(found)
			 updatedParams = new String(updatedParams+"default City, ");
		 else
			 errorList.add(new ErrorManager(new InvalidParameterException(),"The chosen city can't be the new default city, plese try with another one", false));
	}
	
	/**
	 * Metodo che controlla che la nuova data non sia antecedente all'attuale data di default o seguente alla data odierna prima di
	 * procedere all'aggiornamento. In tal caso, aggiunge il parametro tra quello aggiornati con successo, altrimenti aggiunge un nuovo
	 * errore alla lista.
	 * @param date la nuova data di default
	 * @throws java.text.ParseException nel caso di errore nel parsing della stringa in data
	 */
	public void checkAndChangeDefaultDate(String date) throws java.text.ParseException {
		
		Date newDate = Configuration.getDateFormatter().parse(date);
		Date defaultDate = Configuration.getDateFormatter().parse(Configuration.getDefaultStartDate());
		
		if(newDate.before(new Date()) || newDate.after(defaultDate)) {
			Configuration.setDefaultStartDate(date);
			updatedParams = new String(updatedParams+"default Date, ");
		} else 
			errorList.add(new ErrorManager(new InvalidParameterException(),"The chosen date can't be the default date, please try with another one", false));	
	}
	
	/**
	 * Metodo che controlla che il nuovo sistema di misurazione sia tra i tre previsti prima di procedere con l'aggiornamento.
	 * In tal caso, aggiunge il parametro tra quello aggiornati con successo, altrimenti aggiunge un nuovo errore alla lista.
	 * @param unit il nuovo sistema di misurazione
	 */
	public void checkAndChangeDefaultTempUnit(String unit) {
		
		if(unit.equals("Standard") || unit.equals("Metric") || unit.equals("Imperial")) {
			Configuration.setMeasurementSystem(unit);
			updatedParams = new String(updatedParams+"Measurement System, ");
		} else 
			errorList.add(new ErrorManager(new InvalidParameterException(),"The chosen unit isn't correct, correct ones are", false));
	}
	
	/**
	 * Metodo che controlla che il nuovo nome del file database includa l'estensione del formato JSON prima di procedere con l'aggiornamento.
	 * In tal caso, aggiunge il parametro tra quello aggiornati con successo, altrimenti aggiunge un nuovo errore alla lista.
	 * @param name il nuovo nome del database
	 */
	public void checkAndChangeDatabaseFileManager(String name) {
		if(name.contains(".json")) {
			Configuration.setDatabaseFilename(name);
			updatedParams = new String(updatedParams+"Database filename, ");
		} else {
			errorList.add(new ErrorManager(new InvalidParameterException(),"The chosen name can't be the new database name, please try with another one", false));
		}
	}
	
	/**
	 * Metodo che controlla che il nuovo zoom abbia un valore compreso tra '5' e '30' prima di procedere con l'aggiornamento.
	 * In tal caso, aggiunge il parametro tra quello aggiornati con successo, altrimenti aggiunge un nuovo errore alla lista.
	 * @param zoom il nuovo valore dello zoom
	 */
	public void checkAndChangeZoom(int zoom) {
		if(zoom<5)
			errorList.add(new ErrorManager(new InvalidParameterException(),"The chosen zoom in too small, try with a bigger one (min is '5')", false));
		else if(zoom>30)
			errorList.add(new ErrorManager(new InvalidParameterException(),"The chosen zoom in too big, try with a smaller one (max is '30')", false));
		else {
			Configuration.setDefaultZoom(zoom);
			updatedParams = new String(updatedParams+="default Zoom.");
		}
	}

	/**
	 * Metodo che agisce sul thread gestito da timer e sul database in base all'azione scelta dall'utente, che costituisce il primo parametro della
	 * funzione. Le azioni possibili sono: "start", "stop" (oppure "end"), "perform" e "status" (oppure "info").
	 * @param action l'azione da compiere
	 * @param forced per forzare il riavvio del thread nel caso in cui sia già in esecuzione
	 * @return informazioni sull'esito della richiesta e\o sullo stato del timer
	 */
	public Object timerTaskService(String action, boolean forced) {
		HashMap<String,Object> hashmap = new HashMap<String,Object>();
		hashmap.put("message", "No internal errors occurred");
		
		if (action.equals("start")) {
			if(ThreadManager.isRunning()) {
				hashmap.put("info", "Timed thread was already running");
				if(forced) 
					hashmap.put("result", "Timed thread was restarted");
				else
					hashmap.put("result", "Timed thread was not restarted");
			} else {
				hashmap.put("info", "Timed thread was not running");
				hashmap.put("result", "Timed thread was started");
			}
			threadManager.startThread(forced);
			return hashmap;
			
		} else if (action.equals("stop") || action.equals("end")) {
			if(ThreadManager.isRunning()) {
				hashmap.put("info", "Timed thread was running");
				hashmap.put("result", "Timed thread was stopped");
			} else {
				hashmap.put("info", "Timed thread was not running");
				hashmap.put("result", "No actions performed");
			}
			return hashmap;
			
		} else if (action.equals("perform")) {
			try {
				databaseManager.updateDatabase();
			} catch (Exception e) {
				return new ErrorManager(e, "An error occurred while updating the database", true);
			}
			hashmap.put("info", "The database filename is "+Configuration.getDatabaseFilename());
			hashmap.put("result", "The database was updated");
			return hashmap;
			
		} else if (action.equals("status") || action.equals("info")) {
			hashmap.put("runningstatus", ThreadManager.isRunning());
			
			HashMap<String,Object> delay = new HashMap<String,Object>();
			hashmap.put("delay", delay);
			delay.put("initial", Configuration.getDefaultInitialThreadDelay());
			delay.put("standard", Configuration.getDefaultThreadDelay());
			return hashmap;
		}
		
		return new ErrorManager(new InvalidParameterException(), "Invalid parameter: "+action+" is not a valid action", false);
	}
	
}