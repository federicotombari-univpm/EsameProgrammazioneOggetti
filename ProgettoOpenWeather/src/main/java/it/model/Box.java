package it.model;

import it.configuration.Configuration;
import it.exception.InvalidParameterException;

public class Box {

	private Coordinates maxCoords;
	private Coordinates minCoords;
	private int zoom;
	
	// i costruttori saranno entrambi utilizzati da chiamate diverse alla nostra API
	public Box(Coordinates maxCoords, Coordinates minCoords) {
		this.maxCoords = maxCoords;
		this.minCoords = minCoords;
		this.zoom = Configuration.getDefaultZoom();
	}
	
	public Box(double maxLatitude, double maxLongitude, double minLatitude, double minLongitude, int zoom) throws InvalidParameterException {
		maxCoords = new Coordinates(maxLatitude, maxLongitude);
		minCoords = new Coordinates(minLatitude, minLongitude);
		this.zoom = Math.abs(zoom);
	}

	public Coordinates getMaxCoords() {
		return maxCoords;
	}

	public void setMaxCoords(Coordinates maxCoords) {
		this.maxCoords = maxCoords;
	}

	public Coordinates getMinCoords() {
		return minCoords;
	}

	public void setMinCoords(Coordinates minCoords) {
		this.minCoords = minCoords;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
}
