package it.configuration;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.exception.WebServiceException;
import it.utilities.ThreadManager;
import it.utilities.Utilities;

/**
 * Classe che si occupa della gestione di gran parte delle eccezioni che vengono lanciate da metodi delle altre classi del progetto.
 * Essa consiste di un costruttore per definire il tipo di errore in base all'eccezione, e di un metodo per scrivere un log nel caso in
 * cui venga richiesto, oltre che dei metodi getter e setter per i quattro attributi della classe.
 * @author JoshuaSgariglia
 */
public class ErrorManager {

	private int errorId;
	private String info;
	private String message;
	private String timestamp;
	
	/**
	 * Unico costruttore della classe: inizialmente popola gli attributi 'info' in base all'eccezione e 'timestamp' con la data e l'ora di
	 * quel momento, utilizzando el secondo caso un metodo statico della classe 'Utilities', nell'omonimo package; successivamente,
	 * attraverso una serie di controlli, attribuisce dei valori agli attributi 'errorId' (codice dell'errore) e 'message' (messaggio predefinito) in base
	 * all'eccezione. Il valore dell'attributo 'message' rimane quello di default a meno che il parametro 'message' sia diverso da una stringa vuota;
	 * infine, eventualmente chiama il metodo 'log' per scrivere un log di errore in formato TXT tramite l'utilizzo di java.io.PrintWriter con informazioni
	 * sull'eccezione e con uno "screenshot" dei parametri di configurazione.
	 * @param e l'eccezione sollevata in un altra classe
	 * @param message il messaggio opzionale relativo all'eccezione
	 * @param writeLog per richiedere di scrivere o meno un log di errore
	 */
	public ErrorManager(Exception e, String message, boolean writeLog) {
		
		info = e.toString();
		timestamp = Utilities.getCurrentDateToString(false);
		
		if(e instanceof FileNotFoundException) {
			errorId = 300;
			this.message = "An error occurred while searching for the database or the configuration file";
		}
		
		else if(e instanceof IOException) {
			errorId = 301;
			this.message = "An error occurred while requesting or sending data to the database or to the web server";
		}
		
		else if(e instanceof org.json.simple.parser.ParseException) {
			errorId = 302;
			this.message = "Internal error: failed to parse JSON data";
		}
		
		else if(e instanceof java.text.ParseException) {
			errorId = 303;
			this.message = "Internal error: failed to convert a String into a Date object";
		}
		
		else if(e instanceof ClassCastException || e instanceof NullPointerException) {
			errorId = 304;
			this.message = "Internal error";
		}
		
		else if(e instanceof DataNotFoundException) {
			errorId = 305;
			this.message = "No data available, please choose different parameters";
		}
		
		else if(e instanceof InvalidParameterException) {
			errorId = 306;
			this.message = "Invalid parameter(s)";
		}
		
		else if(e instanceof WebServiceException) {
			errorId = 307;
			this.message = "An error occurred while getting the data from the web server";
		}
		
		// caso generale
		else {
			errorId = 320;
			this.message = "An error occurred";
		}
		
		if(!message.equals(""))
			this.message = message;
		
		if (writeLog)
			this.log(e);
	}
	
	// metodo per scrivere un log di errore
	private void log(Exception e) {
		try {
			Configuration.increaseErrorLogCounter();
			String filename = "errorlog_"+Configuration.getErrorLogCounter()+".txt";
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, false)));
			
			writer.println("-- Error Log "+Configuration.getErrorLogCounter()+" --");
			writer.println("Timestamp: "+timestamp);
			writer.println();
			writer.println("(1) Error Management");
			writer.println("- Error Info -");
			writer.println("Error Id:      "+errorId);
			writer.println("Error Type:    "+info);
			writer.println("Error Message: "+message);
			writer.println();
			writer.println("- Exception StackTrace -");
			
			StringWriter stringWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter));
			writer.println(stringWriter.toString());
			stringWriter.close();
			writer.println();
			
			writer.println("(2) Configuration Settings");
			writer.println("- Main Settings -");
			writer.println("Api Key:             " +Configuration.getApiKey());
			writer.println("Measurement System:  " +Configuration.getMeasurementSystem());
			writer.println("Default City:        " +Configuration.getDefaultCity());
			writer.println("Default Zoom:        " +Configuration.getDefaultZoom());
			writer.println("Default Start Date:  " +Configuration.getDefaultStartDate());
			writer.println("Default Periodicity: " +Configuration.getDefaultPeriodicity());
			writer.println();
			writer.println("- Timer settings - ");
			writer.println("Initial Thread Delay:  " +Configuration.getDefaultInitialThreadDelay()+" seconds");
			writer.println("Standard Thread Delay: " +Configuration.getDefaultThreadDelay()+" seconds");
			writer.println("Running status:        " +ThreadManager.isRunning());
			writer.println();
			writer.println("- File Settings -");
			writer.println("Config Filename:   " +Configuration.getConfigurationFilename());
			writer.println("Database Filename: " +Configuration.getDatabaseFilename());
			writer.println();
			writer.println("- Default City List - ");
			if (Configuration.getDefaultCityList() == null) {
				writer.println("Size: 0");
				writer.println("List: -");
			} else {
				writer.println("Size: " +Configuration.getDefaultCityList().size());
				writer.print("List: ");
				for (String name : Configuration.getDefaultCityList())
					writer.print(name+"; ");
				writer.println();
			}
			writer.close();
			
		} catch (IOException ioe) {
			System.out.println("The application failed to write an error log:");
			ioe.printStackTrace();
		}
	}

	public int getErrorId() {
		return errorId;
	}

	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}