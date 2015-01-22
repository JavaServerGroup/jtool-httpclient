package com.jtool.http.exception;

public class RequestBeanErrorException extends RuntimeException {
	public RequestBeanErrorException(ReflectiveOperationException e) {
		super(e);
	}
	private static final long serialVersionUID = 1L;
}
