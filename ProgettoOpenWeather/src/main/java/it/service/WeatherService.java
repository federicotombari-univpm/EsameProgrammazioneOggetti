package it.service;

import java.io.IOException;
import java.util.Vector;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.DataNotFoundException;
import it.exception.InvalidParametersException;
import it.exception.WebServiceException;
import it.model.Box;
import it.model.City;
import it.model.Coordinates;
import it.utilities.DataDownloader;

@Service
public class WeatherService {

	DataDownloader dataDownloader = new DataDownloader();
	
	public Object getByCityName(String name) { // caso "città singola"
		
		try {
			dataDownloader.chiamataAPI("https://api.openweathermap.org/data/2.5/weather?q="+name+"&appid="+Configuration.getApiKey());
			
		} catch (IOException | ParseException e1) {
			return new ErrorManager(e1, "");
		} catch (DataNotFoundException e2) {
			return new ErrorManager(e2, "Web server returned no data");
		}
		
		switch(dataDownloader.getCode()) {
			case 200:
			try {
				return new City(name, dataDownloader.getCoordinates(-1), dataDownloader.getMain(-1));
				
			} catch (InvalidParametersException e) {
				return new ErrorManager(e, "Latitude can only have values between -90 and +90;\n"
						+ "Longitude can only have values between -180 and +180");
			}
			
			case 400:
				Exception e400 = new InvalidParametersException();
				return new ErrorManager(e400, dataDownloader.getMessage());
			case 401:
				Exception e401 = new WebServiceException();
				return new ErrorManager(e401, "");
			case 404:
				Exception e404 = new InvalidParametersException();
				return new ErrorManager(e404, dataDownloader.getMessage());
			default:			
				Exception exception = new WebServiceException();
				return new ErrorManager(exception, "");
		}
	}

	public Object getByCityCoords(String name, double errorLon, double errorLat) { // caso "box a partire da città"
		try {
			dataDownloader.chiamataAPI("https://api.openweathermap.org/data/2.5/weather?q="+name+"&appid="+Configuration.getApiKey());
			
		} catch (IOException | ParseException e1) {
			return new ErrorManager(e1, "");
		} catch (DataNotFoundException e2) {
			if (errorLon == 0 || errorLat == 0)
				return new ErrorManager(e2, "Web server returned no data: the requested area might be too small"); // vedere
			else
				return new ErrorManager(e2, "Web server returned no data");
		}
		
		Box weatherBox = null;
		try {
			double latitude = dataDownloader.getCoordinates(-1).getLatitude();
			double longitude = dataDownloader.getCoordinates(-1).getLongitude();
			Coordinates maxCoords = new Coordinates(latitude+Math.abs(errorLat), longitude+Math.abs(errorLon));
			Coordinates minCoords = new Coordinates(latitude-Math.abs(errorLat), longitude-Math.abs(errorLon));
			weatherBox = new Box(maxCoords, minCoords);
			
		} catch(InvalidParametersException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90;\n"
					+ "Longitude can only have values between -180 and +180");
		}
		
		return this.getByBoxCoords(weatherBox.getMinCoords().getLongitude(), weatherBox.getMinCoords().getLatitude(),
				weatherBox.getMaxCoords().getLongitude(), weatherBox.getMaxCoords().getLatitude(), weatherBox.getZoom());
	}

	public Object getByBoxCoords(double minLon, double minLat, double maxLon, double maxLat, int zoom) { // caso "box a partire da coordinate"
		
		Box weatherBox = null;
		try {
			weatherBox = new Box(maxLat, maxLon, minLat, minLon, zoom);
			
		} catch (InvalidParametersException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90;\n"
					+ "Longitude can only have values between -180 and +180");
		}
		
		try {
			dataDownloader.chiamataAPI("https://api.openweathermap.org/data/2.5/box/city?bbox="+weatherBox.getMinCoords().getLatitude()
					+","+weatherBox.getMinCoords().getLongitude()+","+weatherBox.getMaxCoords().getLatitude()+","
					+weatherBox.getMaxCoords().getLongitude()+","+weatherBox.getZoom()+"&appid="+Configuration.getApiKey());
			
		} catch (IOException | ParseException e1) {
			return new ErrorManager(e1, "");
		} catch (DataNotFoundException e2) {
			if(zoom < 5)
				return new ErrorManager(e2, "No data available, choose a greater value for 'zoom'");
			else return new ErrorManager(e2, "");
		}
		
		switch(dataDownloader.getCode()) {
			case 200:
				try {
					Vector<City> cityList = new Vector<City>();
					for(int i = 0; i < dataDownloader.getCnt(); i++)
						cityList.add(new City(dataDownloader.getName(i), dataDownloader.getCoordinates(i), dataDownloader.getMain(i)));
					return cityList; 
					
				} catch (InvalidParametersException e) {
					return new ErrorManager(e, "Latitude can only have values between -90 and +90;\n"
							+ "Longitude can only have values between -180 and +180");
				}
		
			case 400:
				Exception e400 = new InvalidParametersException();
				if(dataDownloader.getMessage().equals("Requested area is larger than allowed for your account type (25.00 square degrees)"))
					return new ErrorManager(e400, "Web server returned no data: the requested area might be too wide");
				else
					return new ErrorManager(e400, dataDownloader.getMessage());
			case 401:
				Exception e401 = new WebServiceException();
				return new ErrorManager(e401, "");
			case 404:
				Exception e404 = new InvalidParametersException();
				return new ErrorManager(e404, dataDownloader.getMessage());
			default:			
				Exception exception = new WebServiceException();
				return new ErrorManager(exception, "");
		}
	}

}
