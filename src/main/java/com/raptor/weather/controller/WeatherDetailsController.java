package com.raptor.weather.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raptor.weather.constants.AppConstants;
import com.raptor.weather.context.ProcessingContext;
import com.raptor.weather.response.model.WeatherDetails;
import com.raptor.weather.service.LocationService;
import com.raptor.weather.service.WeatherService;

@RestController
public class WeatherDetailsController {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherDetailsController.class);

	private final LocationService locationService;
	private final WeatherService weatherService;

	public WeatherDetailsController(LocationService locationService, WeatherService weatherService) {
		this.locationService = locationService;
		this.weatherService = weatherService;

	}


    @GetMapping(path = "/weather-by-ip")
	public WeatherDetails getWeatherDetails(
			@RequestParam(name = "ip", defaultValue = AppConstants.DEFAULT_IP) String ip) {

		LOG.info("Recieved Request for ip: {} ", ip);

		var context = new ProcessingContext();
		context.setRequestIP(ip);

		this.locationService.setNextHandler(weatherService);

		this.locationService.process(context);

		var response = new WeatherDetails();
		response.setIp(ip);
		response.setLocation(context.getLocation());
		response.setWeather(context.getWeather());

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			var resp = objectMapper.writeValueAsString(response);
			LOG.info("Response {} ", resp);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return response;


    }

}
