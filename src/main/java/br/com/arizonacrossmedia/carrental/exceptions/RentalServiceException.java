package br.com.arizonacrossmedia.carrental.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RentalServiceException extends RuntimeException {
    public RentalServiceException(String message) {
        super(message);
    }
}
