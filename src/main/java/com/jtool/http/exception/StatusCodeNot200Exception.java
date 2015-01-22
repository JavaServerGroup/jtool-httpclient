package com.jtool.http.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusCodeNot200Exception extends RuntimeException {

	private static final long serialVersionUID = 2977215131256852771L;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String url;
	private int statusCode;
	
	public StatusCodeNot200Exception(String url, int statusCode) {
		this.url = url;
		this.statusCode = statusCode;
		logger.debug(statusCode + "");
	}

	public String getUrl() {
		return url;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
