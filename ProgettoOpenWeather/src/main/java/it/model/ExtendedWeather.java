package it.model;

public class ExtendedWeather extends Weather {

	private String general;
	
	public ExtendedWeather(double pressure, double humidity, double temperature, double visibility, String general) {
		super(pressure, humidity, temperature, visibility);
		this.general = general;
	}

	
	public String getGeneral() {		
		return general;
	}

	public void setGeneral(String general) {
		this.general = general;
	}

}
