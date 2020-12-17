package model;

public class City {

	private String name;
	private Coordinates coords;
	private Weather cityWeather;
	
	public City(String name, Coordinates coords, Weather cityWeather) {
		this.name = name;
		this.coords = coords;
		this.cityWeather = cityWeather;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Coordinates getCoords() {
		return coords;
	}

	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}

	public Weather getCityWeather() {
		return cityWeather;
	}

	public void setCityWeather(Weather cityWeather) {
		this.cityWeather = cityWeather;
	}
}
