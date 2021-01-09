package it;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.utilities.ThreadManager;


@SpringBootApplication
public class ProgettoOpenWeatherApplication {	
	
	public static void main(String[] args) {	
		try {
			Configuration.initializeConfig();
			
		} catch (IOException | ParseException | NullPointerException e) {
			new ErrorManager(e, "An error occurred during initialization", true);
			
		} finally {
			ThreadManager threadManager = new ThreadManager();
			threadManager.startThread(true);
			
			SpringApplication.run(ProgettoOpenWeatherApplication.class, args);
		}
	}

}
