package com.epam.training.gen.ai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GenAiBadRequestException extends RuntimeException {
    public GenAiBadRequestException() {
        super();
    }
    public GenAiBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    public GenAiBadRequestException(String message) {
        super(message);
    }
    public GenAiBadRequestException(Throwable cause) {
        super(cause);
    }
}
