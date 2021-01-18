package it.exception;

/**
 * Classe che estende Exception: si tratta di un'eccezione personalizzata. Viene generalmente lanciata quando a seguito di una
 * richiesta dell'utente attraverso i Controller, il web server genera un errore non meglio specificato da altre eccezioni;
 * in particolare, è possibile trovare questa eccezione nella classe WeatherService.
 * @author JoshuaSgariglia
 */
public class WebServiceException extends Exception {

	private static final long serialVersionUID = -9016507684224740076L; // auto-generated

	/**
	 * Costruttore generico della classe.
	 */
	public WebServiceException() {}
	
}
