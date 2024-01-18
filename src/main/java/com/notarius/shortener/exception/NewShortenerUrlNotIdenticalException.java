package com.notarius.shortener.exception;

import static com.notarius.shortener.exception.FunctionalErrorCode.SHORTENER_URL_CONFLICT;

public class NewShortenerUrlNotIdenticalException extends FunctionalException {

    public NewShortenerUrlNotIdenticalException(final String field) {
        super(SHORTENER_URL_CONFLICT, field);

    }

}
