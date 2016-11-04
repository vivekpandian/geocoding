package com.ncr.app.models;

import com.ncr.app.enumeration.ErrorMessage;

public class ApiErrorResponse extends ApiResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4967283064862058656L;
	private ErrorMessage errorMessage;
	private String shortDescription;
	private String description;

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
