package com.liferay.infrastructure.exceptions;

public class InvalidAdmissionRequestException extends RuntimeException {
    public InvalidAdmissionRequestException() {
    }

    public InvalidAdmissionRequestException(final String message) {
        super(message);
    }
}
