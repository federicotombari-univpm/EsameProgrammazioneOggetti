package it.model;

import it.exception.InvalidParameterException;

public class Coordinates {

	private double latitude;
	private double longitude;
	
	// longitudine: -180, +180
	// latitudine: -90, +90

	public Coordinates(double latitude, double longitude) throws InvalidParameterException {
		if(latitude >= -90 && latitude <=90 && longitude >= -180 && longitude <=180) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
		else throw new InvalidParameterException();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
