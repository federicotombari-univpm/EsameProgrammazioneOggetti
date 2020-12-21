package filter;

import model.Box;

public class WeatherFilter implements Filter {
	
	@Override
	public Object getData() {
		return null;
		
	}
	
	@Override
	public Object getInfo() {
		return null;
		
	}
	
	@Override
	public void checkParams() {
		
	}
	
	public void defineBox(String name, double errorLon, double errorLat) {
		
	}
	
	public void defineBox(double maxLon, double minLon, double maxLat, double minLat, int zoom) {
		Box box = new Box(maxLat, maxLon, minLat, minLon, zoom);
	}
	
}
