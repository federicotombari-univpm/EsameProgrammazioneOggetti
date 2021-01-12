package it.filter;

import java.util.Date;
import java.util.Vector;

import it.configuration.Configuration;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;

public class CheckerImpl extends Operator implements Checker {
	
	public boolean checkDateSpan(Vector<String> dateSpan) throws InvalidParameterException, java.text.ParseException {
		boolean periodicityIsSet = false;
		
		if(dateSpan == null) {
			filterByDateSpan = false;
		}
		
		else switch(dateSpan.size()) {
			case 3:
				this.checkPeriodicity(dateSpan.get(3));
				periodicityIsSet = true;
			case 2:
				startDate = Configuration.getDateFormatter().parse(dateSpan.get(0));
				endDate = Configuration.getDateFormatter().parse(dateSpan.get(1));
				break;
			case 1:
				startDate = Configuration.getDateFormatter().parse(dateSpan.get(0));
				endDate = startDate;
				break;
			default:
				throw new InvalidParameterException();
		}
		
		return periodicityIsSet;	
	}
	
	public void checkDates() throws InvalidParameterException, java.text.ParseException, DataNotFoundException {
		if (startDate.compareTo(endDate) > 0) {
			Date aux = new Date();
			aux = startDate;
			startDate = endDate;
			endDate = aux;
		}
		
		if (startDate.after(new Date()))
			throw new InvalidParameterException();
		
		if (endDate.before(Configuration.getDateFormatter().parse(Configuration.getDefaultDate())))
			throw new DataNotFoundException();
	}
	
	public void checkRequestedWeather(Vector<String> requestedWeather) throws InvalidParameterException {
		if(requestedWeather == null || requestedWeather.get(0).equals("all"))
			this.setWeatherBools(true);
		
		else if(requestedWeather.size() > 4)
			throw new InvalidParameterException();
		
		else {
			boolean noFilterRequested = true;
		
			for(String weatherInfo : requestedWeather) {
				if(weatherInfo.equals("pressure") || weatherInfo.equals("prs")) {
					pressureRequested = true;
					noFilterRequested = false;
				}
				else if(weatherInfo.equals("humidity") || weatherInfo.equals("hum")) {
					humidityRequested = true;
					noFilterRequested = false;
				}
				else if(weatherInfo.equals("temperature") || weatherInfo.equals("tmp")) {
					temperatureRequested = true;
					noFilterRequested = false;
				}
				else if(weatherInfo.equals("visibility") || weatherInfo.equals("vis")) {
					visibilityRequested = true;
					noFilterRequested = false;
				}
			}
			
			if(noFilterRequested)
				this.setWeatherBools(true);
			}
	}
	
	public void checkPeriodicity(String periodicity) throws NumberFormatException {
		if (periodicity.equals("none") || periodicity.equals("all-time") || startDate.equals(endDate))
			filterByPeriodicity = false;
		else if (periodicity.equals("daily"))
			periodicityValue = 1;
		else if (periodicity.equals("weekly"))
			periodicityValue = 7;
		else if (periodicity.equals("monthly"))
			periodicityValue = 30;
		else
			periodicityValue = Math.abs(Integer.parseInt(periodicity));
	}
	
	public void checkCityList(Vector<String> cityList) throws InvalidParameterException, DataNotFoundException {
		this.cityList = new Vector<String>();
		
		if (cityList == null) {
			filterByCityList = false;
			this.cityList = Configuration.getDefaultCityList();
		}
		
		else if (cityList.size() > 50) 
			throw new InvalidParameterException();
		
		else {
			Vector<String> defaultCityList = Configuration.getDefaultCityList();
			boolean found;
			for(int i=0; i<cityList.size(); i++) {
				found = false;
				for(int j=0; j<defaultCityList.size() && found == false; j++) {
					if(cityList.get(i).equals(defaultCityList.get(j))) {
						found = true;
						this.cityList.add(cityList.get(i));
						defaultCityList.remove(j);
					}
				}
			}
			if (this.cityList.size() == 0)
				throw new DataNotFoundException();
		}
	}
}
