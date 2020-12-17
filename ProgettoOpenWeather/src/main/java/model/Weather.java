package model;

public class Weather {

	private double pressure;
	private double humidity;
	private double temperature;
	private double visibility;
	
	public Weather(double pressure, double humidity, double temperature, double visibility) {
		this.pressure = pressure;
		this.humidity = humidity;
		this.temperature = temperature;
		this.visibility = visibility;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getVisibility() {
		return visibility;
	}

	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}

}
