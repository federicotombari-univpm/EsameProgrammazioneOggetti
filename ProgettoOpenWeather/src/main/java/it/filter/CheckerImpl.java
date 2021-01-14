package it.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import it.configuration.Configuration;
import it.exception.DataNotFoundException;
import it.exception.InvalidParameterException;

public class CheckerImpl extends Operator implements Checker {
	
	public CheckerImpl() throws ParseException {
		super();
	}

	public void checkDateSpan(Vector<String> dateSpan) throws InvalidParameterException, java.text.ParseException {
		if(dateSpan == null) {
			filterByDateSpan = false;
		}
		
		else switch(dateSpan.size()) {
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
		};	
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
		
		if (endDate.before(Configuration.getDateFormatter().parse(Configuration.getDefaultStartDate())))
			throw new DataNotFoundException();
	}
	
	public void checkPeriodicity(String periodicity) throws NumberFormatException {
		if (periodicity.equals("daily") || startDate.equals(endDate))
			periodicityValue = 1;
		else if (periodicity.equals("weekly"))
			periodicityValue = 7;
		else if (periodicity.equals("monthly"))
			periodicityValue = 30;
		else if (periodicity.equals("none"))
			return;
		else
			periodicityValue = Math.abs(Integer.parseInt(periodicity));
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
	
	public void checkCityList(Vector<String> cityList) throws InvalidParameterException, DataNotFoundException {
		this.cityList = new Vector<String>();
		
		if (cityList == null) {
			filterByCityList = false;
		}
		
		else if (cityList.size() > 20) 
			throw new InvalidParameterException();
		
		else {
			Vector<String> defaultCityList = Configuration.getDefaultCityList();
			boolean found;
			
			for (String defaultCity : defaultCityList) {
				Iterator<String> iterator = cityList.iterator();
				
				found = false;
				while (iterator.hasNext() && found == false) {
					String city = iterator.next();
					if (city.equals(defaultCity)) {
						found = true;
						this.cityList.add(city);
						iterator.remove();
					}
				}
			}
		}
	}
	
	public void checkSortingType(String sortingType) {
		if (sortingType.length() == 9) {
			
			String mainType = sortingType.substring(0,3);
			
			if (mainType.equals("max") ||  mainType.equals("Max")) {
				sortingType_main = new String("max->min");
				this.checkWeatherSorting(sortingType);
			
			} else if (mainType.equals("min") || mainType.equals("Min")) {
				sortingType_main = new String("min->max");
				this.checkWeatherSorting(sortingType);
				
			} else {
				sortFilteredData = false;
			}	
			
		} else if (sortingType.equals("atoz") ||  sortingType.equals("AtoZ")) {
			sortingType_main = new String("a->z");
		} else if (sortingType.equals("ztoa") || sortingType.equals("ZtoA")) {
			sortingType_main = new String("z->a");
			
		} else {
			sortFilteredData = false;
		}
	}
		
	public void checkWeatherSorting(String sortingType) {
		String weatherType = sortingType.substring(3,6);
		
		if ((weatherType.equals("prs") || weatherType.equals("Prs")) && pressureRequested) {
			sortingType_weather = new String("pressure");
			this.checkStatsSorting(sortingType.substring(6,9));
				
		} else if ((weatherType.equals("hum") || weatherType.equals("Hum")) && humidityRequested) {
			sortingType_weather = new String("humidity");
			this.checkStatsSorting(sortingType.substring(6,9));
				
		} else if ((weatherType.equals("tmp") || weatherType.equals("Tmp")) && temperatureRequested) {
			sortingType_weather = new String("temperature");
			this.checkStatsSorting(sortingType.substring(6,9));
				
		} else if ((weatherType.equals("vis") || weatherType.equals("Vis")) && visibilityRequested) {
			sortingType_weather = new String("visibility");
			this.checkStatsSorting(sortingType.substring(6,9));
			
		} else {
			sortFilteredData = false;
		}
	}
	
	public void checkStatsSorting(String statsType) {
		if (statsType.equals("avg") || statsType.equals("Avg")) {
			sortingType_stats = new String("average");
				
		} else if (statsType.equals("var") || statsType.equals("Var")) {
			sortingType_stats = new String("variance");
			
		} else {
			sortFilteredData = false;
		}
	}
}

