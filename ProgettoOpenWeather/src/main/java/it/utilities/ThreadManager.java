package it.utilities;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.parser.ParseException;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.DataNotFoundException;

public class ThreadManager extends TimerTask {

	private DatabaseManager databaseManager = null;
	private Timer timer = null;
	
	private static boolean running = false;
	
	// costruttore
	public ThreadManager() {
		super();
		databaseManager = new DatabaseManager();
	}
	
	// metodi
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
	
	public void endThread() {
		if(running) {
			timer.cancel();
			running = false;
		}
	}
	
	public static boolean isRunning() {
		return running;
	}

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
