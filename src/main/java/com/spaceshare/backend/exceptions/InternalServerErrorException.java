package com.spaceshare.backend.exceptions;

public class InternalServerErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
