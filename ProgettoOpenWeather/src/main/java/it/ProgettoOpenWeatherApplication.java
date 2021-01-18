package it;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.utilities.ThreadManager;

/**
 * Classe principale del progetto, da cui si può avviare il programma. Si tratta della classe che contiene il 'main'.
 * Non include altri metodi o attributi.
 * @author JoshuaSgariglia
 */
@SpringBootApplication
public class ProgettoOpenWeatherApplication {	
	
	/**
	 * Metodo 'main' del progetto, che inizializza la classe Configuration leggendo i dati da un file predefinito prima di avviare la
	 * SpringApplication con il metodo 'run'. In caso di errore nella ricerca o nella lettura del file di configurazione, il programma
	 * viene avviato ugualmente mantenendo i parametri di default.
	 * @param args
	 */
	public static void main(String[] args) {	
		try {
			Configuration.initializeConfig();
			
		} catch (IOException ioe) {
			new ErrorManager(ioe, "An input/output error occurred during initialization", true);
		} catch (ParseException | NullPointerException | ClassCastException e) {
			new ErrorManager(e, "An internal error occurred during initialization", true);

		} finally {
			ThreadManager threadManager = new ThreadManager(); 		// da rimuovere prima della versione finale
			threadManager.startThread(false);						// da rimuovere prima della versione finale
			
			SpringApplication.run(ProgettoOpenWeatherApplication.class, args);
		}
	}

}































































































