package com.notarius.shortener.exception;

import lombok.Getter;

import static java.lang.String.format;

public class FunctionalException extends RuntimeException {
    @Getter
    private final FunctionalErrorCode errorCode;

    FunctionalException(String messageTemplate, FunctionalErrorCode errorCode, Throwable cause, String... arguments) {
        super(format(messageTemplate, arguments), cause);
        this.errorCode = errorCode;
    }

    FunctionalException(FunctionalErrorCode errorCode, String... arguments) {
        this(errorCode.getMessageTemplate(), errorCode, null, arguments);
    }

}