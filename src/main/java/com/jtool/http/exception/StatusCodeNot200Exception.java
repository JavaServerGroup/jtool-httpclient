package com.jtool.http.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusCodeNot200Exception extends RuntimeException {

	private static final long serialVersionUID = 2977215131256852771L;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String url;
	private int statusCode;
	private Map<String, ?> params;
	
	public StatusCodeNot200Exception(String url, int statusCode) {
		this.url = url;
		this.statusCode = statusCode;
		logger.debug(statusCode + "");
	}
	
	public StatusCodeNot200Exception(String url, Map<String, ?> params, int statusCode) {
		this.url = url;
		this.params = params;
		this.statusCode = statusCode;
		logger.debug(statusCode + "");
	}

	public String getUrl() {
		return url;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Map<String, ?> getParams() {
		return params;
	}

	@Override
	public String toString() {
		return "StatusCodeNot200Exception [logger=" + logger + ", url=" + url
				+ ", statusCode=" + statusCode + ", params=" + params + "]";
	}
	
}
