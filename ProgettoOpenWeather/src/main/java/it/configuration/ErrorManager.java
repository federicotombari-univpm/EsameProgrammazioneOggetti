package it.configuration;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
import it.exception.WebServiceException;
import it.utilities.ThreadManager;
import it.utilities.Utilities;

public class ErrorManager {

	private int errorId;
	private String info;
	private String message;
	private String timestamp;
	
	// nel catch di ogni try-catch ci sarà una chiamata a questo costruttore
	public ErrorManager(Exception e, String message, boolean writeLog) {
		
		info = e.toString();
		this.setTimestamp(Utilities.getCurrentDateToString(false));
		
		if(e instanceof FileNotFoundException) {
			errorId = 300;
			this.message = "An error occurred while searching for the database or the configuration file";
		}
		
		else if(e instanceof IOException) {
			errorId = 301;
			this.message = "An error occurred while requesting or sending data to the database or to the web server";
		}
		
		else if(e instanceof org.json.simple.parser.ParseException) {
			errorId = 302;
			this.message = "Internal error: failed to parse JSON data";
		}
		
		else if(e instanceof java.text.ParseException) {
			errorId = 303;
			this.message = "Internal error: failed to convert a String into a Date object";
		}
		
		else if(e instanceof ClassCastException || e instanceof NullPointerException) {
			errorId = 304;
			this.message = "Internal error";
		}
		
		else if(e instanceof DataNotFoundException) {
			errorId = 305;
			this.message = "No data available, please choose different parameters";
		}
		
		else if(e instanceof InvalidParameterException) {
			errorId = 306;
			this.message = "Invalid parameter(s)";
		}
		
		else if(e instanceof WebServiceException) {
			errorId = 307;
			this.message = "An error occurred while getting the data from the web server";
		}
		
		// general case
		else {
			errorId = 320;
			this.message = "An error occurred";
		}
		
		if(!message.equals(""))
			this.message = message;
		
		if (writeLog)
			this.log(e);
	}
		
	public void log(Exception e) {
		try {
			Configuration.increaseErrorLogCounter();
			String filename = "errorlog_"+Configuration.getErrorLogCounter()+".txt";
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, false)));
			
			writer.println("-- Error Log "+Configuration.getErrorLogCounter()+" --");
			writer.println("Timestamp: "+timestamp);
			writer.println();
			writer.println("(1) Error Management");
			writer.println("- Error Info -");
			writer.println("Error Id:      "+errorId);
			writer.println("Error Type:    "+info);
			writer.println("Error Message: "+message);
			writer.println();
			writer.println("- Exception StackTrace -");
			
			StringWriter stringWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter));
			writer.println(stringWriter.toString());
			stringWriter.close();
			writer.println();
			
			writer.println("(2) Configuration Settings");
			writer.println("- Main Settings -");
			writer.println("Api Key:            " +Configuration.getApiKey());
			writer.println("Measurement System: " +Configuration.getDefaultTempUnit());
			writer.println("Default City:       " +Configuration.getDefaultCity());
			writer.println("Default Date:       " +Configuration.getDefaultDate());
			writer.println("Default Zoom:       " +Configuration.getDefaultZoom());
			writer.println();
			writer.println("- Timer settings - ");
			writer.println("Initial Thread Delay:  " +Configuration.getDefaultInitialThreadDelay()+" seconds");
			writer.println("Standard Thread Delay: " +Configuration.getDefaultThreadDelay()+" seconds");
			writer.println("Running status:        " +ThreadManager.isRunning());
			writer.println();
			writer.println("- File Settings -");
			writer.println("Config Filename:   " +Configuration.getConfigurationFilename());
			writer.println("Database Filename: " +Configuration.getDatabaseFilename());
			writer.println();
			writer.println("- Default City List - ");
			if (Configuration.getDefaultCityList() == null) {
				writer.println("Size: 0");
				writer.println("List: -");
			} else {
				writer.println("Size: " +Configuration.getDefaultCityList().size());
				writer.print("List: ");
				for (String name : Configuration.getDefaultCityList())
					writer.print(name+"; ");
				writer.println();
			}
			writer.close();
			
		} catch (IOException ioe) {
			System.out.println("The application failed to write an error log:");
			ioe.printStackTrace();
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}