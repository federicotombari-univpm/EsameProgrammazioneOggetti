package it.exception;

/**
 * Classe che estende Exception: si tratta di un'eccezione personalizzata. Viene generalmente lanciata quando, a seguito di una
 * richiesta dell'utente attraverso i Controller, i parametri da lui immessi non sono validi; è possibile trovare questa eccezione
 * anche nel costruttore della classe Coordinates.
 * @author JoshuaSgariglia
 */
public class InvalidParameterException extends Exception {

	private static final long serialVersionUID = -4106033773849601984L; // auto-generated
	
	/**
	 * Costruttore generico della classe.
	 */
	public InvalidParameterException() {}
	
}
