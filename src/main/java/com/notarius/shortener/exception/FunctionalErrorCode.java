package com.notarius.shortener.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static java.lang.String.format;

@Getter
public enum FunctionalErrorCode {

    BAD_REQUEST(0, HttpStatus.BAD_REQUEST, "Malformed request"),
    NOT_FOUND_ENTITY_BY_FIELD(1, HttpStatus.NOT_FOUND, "No url of type %s with property %s = %s found"),
    ALREADY_EXIST(2, HttpStatus.FOUND, "url with property %s = %s already exist"),
    SHORTENER_URL_TOO_LONG(3, HttpStatus.URI_TOO_LONG, "Generated shortener URL is too long for URL:%s"),
    SHORTENER_URL_CONFLICT(4, HttpStatus.CONFLICT, "Generated shortener URL does not match a previously generated URL for URL:%s");

    private final String code;
    private final HttpStatus httpStatus;
    private final String messageTemplate;

    FunctionalErrorCode(int code, HttpStatus httpStatus, String messageTemplate) {
        this.code = format("%03d", code);
        this.httpStatus = httpStatus;
        this.messageTemplate = messageTemplate;
    }
}
