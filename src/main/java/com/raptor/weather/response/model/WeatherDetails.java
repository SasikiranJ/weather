package com.raptor.weather.response.model;

public class WeatherDetails {

    private String ip;

    private Location location;

    private Weather weather;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

	@Override
	public String toString() {
		return "WeatherDetails [ip=" + ip + ", location=" + location + ", weather=" + weather + "]";
	}
}
