package com.spaceshare.backend.exceptions;

public class DuplicateResourceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DuplicateResourceException() {
        super();
    }

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
