package com.ncr.app.gc.test;

import org.mockito.ArgumentMatcher;

import com.google.maps.model.LatLng;

public class LatLngMatcher extends ArgumentMatcher<LatLng> {

	private final LatLng latLng;

	public LatLngMatcher(LatLng latLng) {
		this.latLng = latLng;
	}

	@Override
	public boolean matches(Object latLng) {
		LatLng other = (LatLng) latLng;
		if (other == null)
			return false;
		return other.lat == this.latLng.lat && other.lng == this.latLng.lng;
	}

}
