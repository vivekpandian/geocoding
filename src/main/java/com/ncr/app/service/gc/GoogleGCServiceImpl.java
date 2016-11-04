package com.ncr.app.service.gc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.maps.model.LatLng;
import com.ncr.app.exception.GeoCodingException;
import com.ncr.app.util.gc.GoogleMapsGeoCodingClient;

@Component
public class GoogleGCServiceImpl implements GeoCodingService {

	@Autowired
	private GoogleMapsGeoCodingClient gcClient;

	@Override
	@Cacheable(value = "revGeoCodeCache", key = "{#latitude.toString(), #longitutude.toString()}")
	public String getFormattedAddress(Double latitude, Double longitutude) throws GeoCodingException {
		try {
			if (latitude == null || longitutude == null || latitude == 0.0d || latitude == -0.0d || longitutude == 0.0d
					|| longitutude == -0.0d) {
				throw new GeoCodingException("Invalid Params");
			}
			LatLng latLng = new LatLng(latitude, longitutude);
			String address = gcClient.getFormattedAddressForCoords(latLng);
			return address;
		} catch (Exception exception) {
			throw new GeoCodingException(exception);
		}
	}

}
