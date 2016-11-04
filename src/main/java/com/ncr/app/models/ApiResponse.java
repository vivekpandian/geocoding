package com.ncr.app.models;

import java.io.Serializable;

public class ApiResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8591528053975318040L;
	private long timeStamp;

	public ApiResponse() {
		this.timeStamp = System.currentTimeMillis();
	}

	public long getTimeStamp() {
		return timeStamp;
	}

}
