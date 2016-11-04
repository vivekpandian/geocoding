package com.ncr.app.models;

public class RevGeoCodingResponse extends ApiResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1518859112656213698L;
	private String formattedAddress;

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

}
