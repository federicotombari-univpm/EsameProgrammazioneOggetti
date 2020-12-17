package model;

public class Box {

	private Coordinates maxCoords;
	private Coordinates minCoords;
	
	// i costruttori saranno entrambi utilizzati da chiamate diverse alla nostra API
	public Box(Coordinates maxCoords, Coordinates minCoords) {
		this.maxCoords = maxCoords;
		this.minCoords = minCoords;
	}
	
	public Box(double maxLatitude, double maxLongitude, double minLatitude, double minLongitude) {
		maxCoords = new Coordinates(maxLatitude, maxLongitude);
		minCoords = new Coordinates(minLatitude, minLongitude);
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
}
