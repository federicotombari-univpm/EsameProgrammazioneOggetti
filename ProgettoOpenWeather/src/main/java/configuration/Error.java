package configuration;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;
// altre eccezioni da importare

public class Error {

	private int errorId;
	private String info;
	private String message;
	
	// nel catch di ogni try-catch ci sarà una chiamata a questo costruttore
	public Error(Exception e) {
		
		info = e.toString();
		
		if(e instanceof IOException) {
			errorId = 301; // possibile database JSON di codici errore
			message = "An error occurred while loading or updating the database";
		}
		
		else if(e instanceof FileNotFoundException) {
			errorId = 302;
			message = "An error occurred while searching for the database";
		}
		
		else if(e instanceof ParseException) {
			errorId = 303;
			message = "An error occurred while converting the data";
		}
		
		// e così via
		
		else {
			errorId = 350;
			message = "An error occurred";
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
}
