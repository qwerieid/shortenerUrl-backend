package com.notarius.shortener.exception;

import static com.notarius.shortener.exception.FunctionalErrorCode.SHORTENER_URL_TOO_LONG;

public class ShortenerUrlToLongException extends FunctionalException {

    public ShortenerUrlToLongException(final String field) {
        super(SHORTENER_URL_TOO_LONG, field);

    }

}
