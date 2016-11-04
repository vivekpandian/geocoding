package com.ncr.app.enumeration;

public enum ErrorMessage {
	
	MissingParams (100),
	InValidParamsError (101),
	ProcessingError (102),
	UnKnownError (999);
	
	private int id;
	
	ErrorMessage (int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
