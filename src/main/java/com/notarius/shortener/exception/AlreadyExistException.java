package com.notarius.shortener.exception;

import static com.notarius.shortener.exception.FunctionalErrorCode.ALREADY_EXIST;

public class AlreadyExistException extends FunctionalException {

    public AlreadyExistException(String field, String value) {
        super(ALREADY_EXIST, field, value);
    }

}
