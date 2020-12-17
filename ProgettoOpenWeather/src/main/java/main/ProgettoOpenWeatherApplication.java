package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProgettoOpenWeatherApplication {

	// ci sarà bisogno di programmare un'inizializzazione oltre al semplice run di Spring:
	// i parametri di Configuration devono essere letti da un file JSON
	public static void main(String[] args) {
		SpringApplication.run(ProgettoOpenWeatherApplication.class, args);
	}

}
