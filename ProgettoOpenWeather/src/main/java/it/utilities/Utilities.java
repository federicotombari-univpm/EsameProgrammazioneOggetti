package it.utilities;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.configuration.Configuration;

public class Utilities {
	
	public static String getCurrentDateToString(boolean simple) {
		if (simple)
			return Configuration.getDateFormatter().format(new Date());
		else
			return Configuration.getTimeFormatter().format(new Date());
	}
	
	public static Date addDaysToDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	public static Date readAndParseTimestamp(JSONArray data, int index) throws ParseException {
		JSONObject dataElement = (JSONObject) data.get(index);
		String timestamp = (String) dataElement.get("timestamp");
		return Configuration.getDateFormatter().parse(timestamp);
	}
	
	public static void updateList(List<String> list, String string) {
		boolean added = false;
		for(int i=0; i<list.size() && added==false; i++)
			if(list.get(i).equals(string))
				added = true;
		if(!added)
			list.add(string);
	}
	
	public static double roundDouble(double value) {
		return Math.round(value*1000)/1000;
	}
	
	public static double calcAverage(List<Double> list) {
		double sum = 0;
		for (double value : list)
			sum+=value;
		return sum/list.size();
	}
	
	public static double calcVariance(List<Double> list, double average) {
		double sum = 0;
		for (double value : list)
			sum+=Math.pow(value-average, 2);
		return sum/list.size();
		
	}
	
}
