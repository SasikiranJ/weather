package com.raptor.weather.service;

import com.raptor.weather.config.IPInfoApiConfiguration;
import com.raptor.weather.context.ProcessingContext;
import com.raptor.weather.exception.SystemErrorException;
import com.raptor.weather.external.services.models.IPInfoResponse;
import com.raptor.weather.response.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService extends BaseService {
	private static final Logger LOG = LoggerFactory.getLogger(LocationService.class);

	private final RestTemplate restTemplate;

	private final IPInfoApiConfiguration ipInfoConfig;



	@Autowired
	public LocationService(RestTemplate restTemplate, IPInfoApiConfiguration ipInfoConfig) {
		this.restTemplate = restTemplate;
		this.ipInfoConfig = ipInfoConfig;
	}

	@Override
	public void process(ProcessingContext context) {
		var location = new Location();

		IPInfoResponse ipInfoResp = getLocationByIP(context.getRequestIP());

		location.setCity(ipInfoResp.getCity());
		location.setCountry(ipInfoResp.getCountry());

		context.setLocation(location);
		context.setCoordinates(ipInfoResp.getLoc());

		nextService.process(context);

	}

	@Retryable(value = RestClientException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
	public IPInfoResponse getLocationByIP(String ipAddress) {
		try {
			String url = String.format("%s/%s?token=%s", ipInfoConfig.getUrl(), ipAddress, ipInfoConfig.getToken());

			return restTemplate.getForObject(url, IPInfoResponse.class);
		} catch (Exception e) {
			LOG.debug("Exception while getting location by IP: {}", ipAddress);
			throw new SystemErrorException("Error fetching location details", e);
		}
	}

}

