package com.ncr.app.exception;

public class GeoCodingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 772371018867970163L;

	public GeoCodingException(Exception exception) {
		super(exception);
	}

	public GeoCodingException(String msg) {
		super(msg);
	}

	public GeoCodingException(String msg, Exception exception) {
		super(msg, exception);
	}

}
