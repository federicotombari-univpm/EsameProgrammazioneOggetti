package it.service;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import it.configuration.Configuration;
import it.configuration.ErrorManager;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;
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
			
		} catch (IOException e) {
			switch(dataDownloader.getHttpsStatus()) {
				case 400:
					Exception e400 = new InvalidParameterException();
					return new ErrorManager(e400, "");
				case 401:
					Exception e401 = new WebServiceException();
					return new ErrorManager(e401, "Requested data is unavailable, try a different request");
				case 404:
					Exception e404 = new DataNotFoundException();
					return new ErrorManager(e404, "Web server returned no data, city name might be invalid");
				case 429:
					Exception e429 = new WebServiceException();
					return new ErrorManager(e429, "Web server is overloaded, please try again later");
				case -1:
					return new ErrorManager(e, "");
				default:			
					Exception exception = new WebServiceException();
					return new ErrorManager(exception, "");
			}
		} catch (ParseException e2) {
			return new ErrorManager(e2, "");
			
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "Web server returned no data, city name might be invalid");
			
		} catch (Exception generalException) {
			return new ErrorManager(generalException, "");
		}
		
		try {
			return new City(name, dataDownloader.getCoordinates(-1), dataDownloader.getMain(-1));
				
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180");
		}	
	}

	public Object getByCityCoords(String name, List<Double> defineError) { // caso "box a partire da città"
		double errorLon, errorLat;
		switch(defineError.size()) {
			case 2:
				errorLat = defineError.get(0);
				errorLon = defineError.get(1);
				break;
			case 1:
				errorLat = defineError.get(0);
				errorLon = errorLat;
				break;
			case 0:
				Exception exception = new InvalidParameterException();
				return new ErrorManager(exception, "Invalid parameters: all parameters are missing");
			default:
				Exception exception1 = new InvalidParameterException();
				return new ErrorManager(exception1, "Invalid parameters: only 2 parameters are needed");
		}
		
		try {
			dataDownloader.chiamataAPI("https://api.openweathermap.org/data/2.5/weather?q="+name+"&appid="+Configuration.getApiKey());
			
		} catch (IOException e) {
			switch(dataDownloader.getHttpsStatus()) {
				case 400:
					Exception e400 = new InvalidParameterException();
					return new ErrorManager(e400, "");
				case 401:
					Exception e401 = new WebServiceException();
					return new ErrorManager(e401, "Requested data is unavailable, try a different request");
				case 404:
					Exception e404 = new DataNotFoundException();
					return new ErrorManager(e404, "Web server returned no data, city name might be invalid");
				case 429:
					Exception e429 = new WebServiceException();
					return new ErrorManager(e429, "Web server is overloaded, please try again later");
				case -1:
					return new ErrorManager(e, "");
				default:			
					Exception exception = new WebServiceException();
					return new ErrorManager(exception, "");
			}
		} catch (ParseException e2) {
			return new ErrorManager(e2, "");
			
		} catch (DataNotFoundException e3) {
			return new ErrorManager(e3, "Web server returned no data, city name might be invalid");
			
		} catch (Exception generalException) {
			return new ErrorManager(generalException, "");
		}
		
		Box weatherBox = null;
		try {
			double latitude = dataDownloader.getCoordinates(-1).getLatitude();
			double longitude = dataDownloader.getCoordinates(-1).getLongitude();
			Coordinates maxCoords = new Coordinates(latitude+Math.abs(errorLat), longitude+Math.abs(errorLon));
			Coordinates minCoords = new Coordinates(latitude-Math.abs(errorLat), longitude-Math.abs(errorLon));
			weatherBox = new Box(maxCoords, minCoords);
			
		} catch(InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180");
		}
		
		return this.getBoxData(weatherBox.getMinCoords().getLatitude(), weatherBox.getMinCoords().getLongitude(),
				weatherBox.getMaxCoords().getLatitude(), weatherBox.getMaxCoords().getLongitude(), weatherBox.getZoom());
	}
	
	public Object getByBoxCoords(List<Double> defineBox) { // caso "box a partire da coordinate"
		double minLat, minLon, maxLat, maxLon, zoom;
		switch(defineBox.size()) {
			case 5:
				minLat = defineBox.get(0);
				minLon = defineBox.get(1);
				maxLat = defineBox.get(2);
				maxLon = defineBox.get(3);
				zoom   = defineBox.get(4);
				break;
			case 4:
				minLat = defineBox.get(0);
				minLon = defineBox.get(1);
				maxLat = defineBox.get(2);
				maxLon = defineBox.get(3);
				zoom   = Configuration.getDefaultZoom();
				break;
			case 0:
				Exception exception = new InvalidParameterException();
				return new ErrorManager(exception, "Invalid parameters: all parameters are missing");
			default:
				Exception exception1 = new InvalidParameterException();
				return new ErrorManager(exception1, "Invalid parameters: incorrect number of parameters");
		}
		
		Box weatherBox = null;
		try {
			weatherBox = new Box(maxLat, maxLon, minLat, minLon, (int)zoom);
			
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180");
		}
		
		return this.getBoxData(weatherBox.getMinCoords().getLatitude(), weatherBox.getMinCoords().getLongitude(),
				weatherBox.getMaxCoords().getLatitude(), weatherBox.getMaxCoords().getLongitude(), weatherBox.getZoom());
	}

	public Object getBoxData(double minLat, double minLon, double maxLat, double maxLon, int zoom) { // caso box - parte comune
		
		try {
			dataDownloader.chiamataAPI("https://api.openweathermap.org/data/2.5/box/city?bbox="
					+minLat+","+minLon+","+maxLat+","+maxLon+","+zoom+"&appid="+Configuration.getApiKey());
			
		} catch (IOException e) {
			switch(dataDownloader.getHttpsStatus()) {
				case 400:
					Exception e400 = new InvalidParameterException();
					return new ErrorManager(e400, "Invalid parameters: the requested area is too wide");
				case 401:
					Exception e401 = new WebServiceException();
					return new ErrorManager(e401, "Requested data is unavailable, try a different request");
				case 404:
					Exception e404 = new DataNotFoundException();
					if (minLat == maxLat)
						return new ErrorManager(e404, "No data available: min and max latitude have the same value");
					else if (minLon == maxLon)
						return new ErrorManager(e404, "No data available: min and max longitude have the same value");
					else if (zoom < 5)
						return new ErrorManager(e404, "No data available, choose a greater value for 'zoom'");
					else return new ErrorManager(e404, "");
				case 429:
					Exception e429 = new WebServiceException();
					return new ErrorManager(e429, "Web server is overloaded, please try again later");
				case -1:
					return new ErrorManager(e, "");
				default:			
					Exception exception = new WebServiceException();
					return new ErrorManager(exception, "");
			}
		} catch (ParseException e2) {
			return new ErrorManager(e2, "");
			
		} catch (DataNotFoundException e3) {
			if (minLat == maxLat)
				return new ErrorManager(e3, "No data available: min and max latitude have the same value");
			else if (minLon == maxLon)
				return new ErrorManager(e3, "No data available: min and max longitude have the same value");
			else if (zoom < 5)
				return new ErrorManager(e3, "No data available, choose a greater value for 'zoom'");
			else return new ErrorManager(e3, "");
			
		} catch (Exception generalException) {
			return new ErrorManager(generalException, "");
		}
		
		try {
			Vector<City> cityList = new Vector<City>();
			for(int i = 0; i < dataDownloader.getCnt(); i++)
				cityList.add(new City(dataDownloader.getName(i), dataDownloader.getCoordinates(i), dataDownloader.getMain(i)));
			return cityList; 
					
		} catch (InvalidParameterException e) {
			return new ErrorManager(e, "Latitude can only have values between -90 and +90. "
					+ "Longitude can only have values between -180 and +180");
		}
	
	}

}
