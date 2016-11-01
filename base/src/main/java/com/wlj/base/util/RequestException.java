package com.wlj.base.util;

public class RequestException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1357697966549014612L;

	public RequestException() {
		super();
	}

	public RequestException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public RequestException(String detailMessage) {
		super(detailMessage);
	}

	public RequestException(Throwable throwable) {
		super(throwable);
	}

	
}
