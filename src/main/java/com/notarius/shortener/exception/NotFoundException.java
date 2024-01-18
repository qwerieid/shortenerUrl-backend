package com.notarius.shortener.exception;

import static com.notarius.shortener.exception.FunctionalErrorCode.NOT_FOUND_ENTITY_BY_FIELD;

public class NotFoundException extends FunctionalException {

    public NotFoundException(Class<?> entityClass, String field, String value) {
        super(NOT_FOUND_ENTITY_BY_FIELD, entityClass.getSimpleName(), field, value);
    }

}
