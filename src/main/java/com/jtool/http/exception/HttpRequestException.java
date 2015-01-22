package com.jtool.http.exception;

import java.io.IOException;

public class HttpRequestException extends RuntimeException {

	private static final long serialVersionUID = -4167584468866273521L;
	
	private String url;
	private IOException ioException;
	
	public HttpRequestException(String url, IOException e) {
		this.url = url;
		this.ioException = e;
	}
	
	public String getUrl() {
		return url;
	}

	public IOException getIoException() {
		return ioException;
	}

}
