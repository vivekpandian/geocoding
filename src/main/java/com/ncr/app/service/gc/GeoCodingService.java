package com.ncr.app.service.gc;

import com.ncr.app.exception.GeoCodingException;

public interface GeoCodingService {
	public String getFormattedAddress(Double latitude, Double longitutude) throws GeoCodingException;

}
