package com.raptor.weather.context;

import com.raptor.weather.response.model.Location;
import com.raptor.weather.response.model.Weather;

public class ProcessingContext {

	private String requestIP;

	private Location location;

	private Weather weather;

	private String coordinates;

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public void setRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}

}
