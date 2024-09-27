package com.lcwd.electronicstore.exceptions;

@SuppressWarnings("serial")
public class BadApiRequestException extends RuntimeException{
	public BadApiRequestException(String message) {
		super(message);
	}
	public BadApiRequestException() {
	}
}
