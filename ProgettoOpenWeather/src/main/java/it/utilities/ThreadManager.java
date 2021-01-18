package it.utilities;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.parser.ParseException;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.DataNotFoundException;

/**
 * Classe per la gestione di un Thread comandato da un Timer; per fare ciò, la classe implementa TimerTask. Gli attributi consistono di
 * un oggetto DatabaseManager (il Thread riguarda questa classe), un oggetto Timer e un boolean per monitorare lo stato del Thread. Le eccezioni
 * sono gestite tramite throws e try-catch con ErrorManager.
 * @author JoshuaSgariglia
 */
public class ThreadManager extends TimerTask { // TimerTask implementa Runnable, l'interfaccia dei Thread

	// attributi
	private DatabaseManager databaseManager = null;
	private Timer timer = null;
	
	private static boolean running = false;
	
	// costruttore
	/**
	 * Unico costruttore della classe, che richiama quello della superclasse, TimerTask, e inizializza l'attributo 'databaseManager'.
	 */
	public ThreadManager() {
		super();
		databaseManager = new DatabaseManager();
	}
	
	// metodi
	/** Metodo per far partire il thread a tempo. Inizialmente controlla se il thread è già in funzione, in tal caso può forzarne l'arresto
	 * e farlo ripartire (se il parametro 'forced' è 'true'). Nei casi in cui il thread non è in funzione o viene fatto ripartire, si
	 * crea un nuovo Timer e si aziona il thread (metodo 'run') in maniera periodica in base ai parametri 'defaultInitialThreadDelay' e
	 * 'defaultThreadDelay' di Configuration.
	 * @param forced per forzare o meno un nuovo avvio del thread nel caso sia già in funzione
	 * @throws IllegalArgumentException se almeno uno dei valori dei parametri del metodo 'schedule' della classe Timer (in 'java.util') non è valido
	 */
	public void startThread(boolean forced) throws IllegalArgumentException {
		if(!running) {
			timer = new Timer();
			timer.schedule(this, Configuration.getDefaultInitialThreadDelay()*1000, Configuration.getDefaultThreadDelay()*1000);
			running = true;
		} else if (forced) {
			this.cancel();
			this.startThread(false);
		}
	}
	
	/**
	 * Metodo che controlla che il thread a tempo sia in funzione, e in tal caso lo termina.
	 */
	public void endThread() {
		if(running) {
			timer.cancel();
			running = false;
		}
	}
	
	/**
	 * Metodo per ottenere lo stato del thread a tempo (attributo 'running').
	 * @return lo stato del thread
	 */
	public static boolean isRunning() {
		return running;
	}

	/**
	 * Metodo 'run' che costituisce il thread, gestito da un Timer. Aggiorna il database periodicamente e gestisce le eccezioni
	 * lanciate dal metodo 'updateDatabase' di 'databaseManager' attraverso try-catch e ErrorManager.
	 */
	@Override
	public void run() {
		
		try {
			databaseManager.updateDatabase();
			
		} catch (DataNotFoundException e) {
			new ErrorManager(e, "Web server returned no data", true);
		} catch (IOException e) {
			new ErrorManager(e, "An input/output error occurred while updating the database", true);
		} catch (ParseException | ClassCastException e) {
			new ErrorManager(e, "An internal error occurred while updating the database", true);
		} 
	}
	
}
