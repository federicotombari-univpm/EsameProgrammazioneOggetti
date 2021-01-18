package it.exception;

/**
 * Classe che estende Exception: si tratta di un'eccezione personalizzata. Viene generalmente lanciata quando a seguito di un
 * HttpsStatus 404-NotFound (nel caso del web server) oppure per via di parametri scelti dall'utente (nel caso delle statistiche)
 * non si hanno dati disponibili.
 * @author JoshuaSgariglia
 */
public class DataNotFoundException extends Exception {

	private static final long serialVersionUID = -7373246304044750239L; // auto-generated

	/**
	 * Costruttore generico della classe.
	 */
	public DataNotFoundException() {}
	
}
