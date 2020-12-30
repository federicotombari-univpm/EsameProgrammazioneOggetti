package it.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.exception.DataNotFoundException;
import it.exception.InvalidParametersException;
import it.exception.WebServiceException;

public class ErrorManager {

	private int errorId;
	private String info;
	private String message;
	
	// nel catch di ogni try-catch ci sarà una chiamata a questo costruttore
	public ErrorManager(Exception e, String message) {
		
		info = e.toString();
		
		if(e instanceof FileNotFoundException) {
			errorId = 300;
			this.message = "An error occurred while searching for the database";
		}
		
		else if(e instanceof IOException) {
			errorId = 301;
			this.message = "An error occurred while requesting or sending data to the database or to the web server";
		}
		
		else if(e instanceof ParseException) {
			errorId = 302;
			this.message = "An error occurred while parsing the data";
		}
		
		else if(e instanceof ClassCastException || e instanceof NullPointerException) {
			errorId = 303;
			this.message = "Internal error";
		}
		
		else if(e instanceof DataNotFoundException) {
			errorId = 304;
			this.message = "No data available, please choose different parameters";
		}
		
		else if(e instanceof InvalidParametersException) {
			errorId = 305;
			this.message = "Invalid parameters";
		}
		
		else if(e instanceof WebServiceException) {
			errorId = 306;
			this.message = "An error occurred while getting the data from the web server";
		}
		
		// e così via
		
		else {
			errorId = 350;
			this.message = "An error occurred";
		}
		
		if(!message.equals("")) {
			this.message = message;
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