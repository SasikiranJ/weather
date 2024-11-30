package com.raptor.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.raptor.weather.config.OpenWeatherMapApiConfiguration;
import com.raptor.weather.context.ProcessingContext;
import com.raptor.weather.external.services.models.WeatherResponse;
import com.raptor.weather.response.model.Weather;

@Service
public class WeatherService extends BaseService {

	private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

	private final RestTemplate restTemplate;

	private final OpenWeatherMapApiConfiguration weatherApiConfig;

	@Autowired
	public WeatherService(RestTemplate restTemplate, OpenWeatherMapApiConfiguration weatherApiConfig) {
		this.restTemplate = restTemplate;
		this.weatherApiConfig = weatherApiConfig;
	}

	@Override
	public void process(ProcessingContext context) {

		String latitude = null;
		String longitude = null;
		if (context.getCoordinates() != null) {

			String[] coords = context.getCoordinates().split(",");
			latitude = coords[0].trim();
			longitude = coords[1].trim();

			var weather = new Weather();
			var weatherResponse = getWeatherByCoords(latitude, longitude);

			if (weatherResponse != null && weatherResponse.getMain() != null) {
				weather.setTemperature(weatherResponse.getMain().getTemp());
				weather.setHumidity(weatherResponse.getMain().getHumidity());
				if (!CollectionUtils.isEmpty(weatherResponse.getWeather())) {
					weather.setDescription(weatherResponse.getWeather().get(0).getDescription());
				}
			}

			context.setWeather(weather);
		}


	}

	public WeatherResponse getWeatherByCoords(String latitude, String longitude) {

		try {
			String url = String.format("%s?lat=%s&lon=%s&appid=%s", weatherApiConfig.getUrl(), latitude, longitude,
					weatherApiConfig.getKey());

			LOG.debug("Weather Service URL {}", url);

			return restTemplate.getForObject(url, WeatherResponse.class);
		} catch (Exception e) {
			LOG.debug("Exception while retrieving weather details for coordinates {}, {}", latitude, longitude);
			throw new RuntimeException("Error fetching location details ", e);
		}
	}

}
