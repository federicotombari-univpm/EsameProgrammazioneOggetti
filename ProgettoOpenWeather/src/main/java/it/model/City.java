package it.model;

/**
 * Classe che definisce il model di City, composto da un attributo di tipo Coordinates, uno di tipo Weather e una stringa (il nome della
 * città). La classe include un costruttore e i metodi getter e setter. Viene utilizzata nella "parte Weather" del progetto, in particolare in
 * WeatherService (package 'service').
 * @author JoshuaSgariglia
 */
public class City {

	// attributi
	private String name;
	private Coordinates coords;
	private Weather weather;
	
	// costruttore
	/**
	 * Costruttore della classe, che associa ad ogni attributo un omonimo parametro. I tipi dei parametri sono gli stessi di quelli degli
	 * attributi della classe a cui si riferiscono.
	 * @param name il nome della città
	 * @param coords le coordiante della città
	 * @param weather le condizioni meteo della città
	 */
	public City(String name, Coordinates coords, Weather weather) {
		this.name = name;
		this.coords = coords;
		this.weather = weather;
	}

	// metodi getter e setter
	/**
	 * Metodo getter dell'attributo 'name'.
	 * @return il nome della città
	 */
	public String getName() {
		return name;
	}

	/**
	 * Metodo setter dell'attributo 'name'
	 * @param name il nuovo nome da assegnare alla città
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Metodo getter dell'attributo 'coords'.
	 * @return le coordinate della città
	 */
	public Coordinates getCoords() {
		return coords;
	}

	/**
	 * Metodo setter dell'attributo 'coords'
	 * @param coords le coordinate della città
	 */
	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}

	/**
	 * Metodo getter dell'attributo 'weather'.
	 * @return il meteo della città
	 */
	public Weather getCityWeather() {
		return weather;
	}

	/**
	 * Metodo setter dell'attributo 'weather'
	 * @param cityWeather le nuove informazioni meteo della città
	 */
	public void setCityWeather(Weather cityWeather) {
		this.weather = cityWeather;
	}
}
