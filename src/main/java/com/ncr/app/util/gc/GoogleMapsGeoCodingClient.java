package com.ncr.app.util.gc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.ncr.app.exception.GeoCodingException;

@Component
public class GoogleMapsGeoCodingClient {
	@Value("${geooglemaps.geocoding.apiKey}")
	private String apiKey;
	private GeoApiContext geoApiContext;

	@PostConstruct
	public void initGoogleMapsApi() {
		geoApiContext = new GeoApiContext().setApiKey(apiKey);
	}

	public String getFormattedAddressForCoords(LatLng latLng) throws GeoCodingException {
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.newRequest(geoApiContext).latlng(latLng).await();
			if (results == null || results.length == 0) {
				throw new GeoCodingException("Invalid Latitude and/or Longitutde");
			}
		} catch (Exception exception) {
			throw new GeoCodingException("Exception from Google Maps for reverse Geocode", exception);
		}
		return results[0].formattedAddress;
	}

}
