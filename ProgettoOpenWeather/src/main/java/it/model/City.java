package it.model;

public class City {

	private String name;
	private Coordinates coords;
	private Weather weather;
	
	public City(String name, Coordinates coords, Weather weather) {
		this.name = name;
		this.coords = coords;
		this.weather = weather;
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
		return weather;
	}

	public void setCityWeather(Weather cityWeather) {
		this.weather = cityWeather;
	}
}
