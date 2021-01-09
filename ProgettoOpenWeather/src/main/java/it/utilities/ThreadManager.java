package it.utilities;

import java.util.Timer;
import java.util.TimerTask;

import it.configuration.Configuration;
import it.configuration.ErrorManager;

public class ThreadManager extends TimerTask {

	DatabaseManager DM = null;
	Timer timer = null;
	
	boolean running = false;
	
	// costruttore
	public ThreadManager() {
		super();
		DM = new DatabaseManager();
	}
	
	// metodi
	public void startThread(boolean forced) {
		if(!running) {
			timer = new Timer();
			timer.schedule(this, 0, Configuration.getDefaultThreadDelay()*1000);
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
	
	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		try {
			DM.updateDatabase();
		} catch (Exception e) {
			new ErrorManager(e, "An error occurred while updating the database", true);		}
	}
}
