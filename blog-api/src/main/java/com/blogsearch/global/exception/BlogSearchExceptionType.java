package com.blogsearch.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

@Getter
@RequiredArgsConstructor
public enum BlogSearchExceptionType {

    //global
    INVALID_REQUEST("ERR-001", 400, "invalid request: {0}"),
    NOT_FOUND("ERR-002", 404, "not found: {0}"),

    CONSTRAINT_VIOLATION("ERR-010", 400, "request parameter constraint violation"),
    CONNECT_TIME_OUT("ERR-011", 408, "{0}"),
    READ_TIME_OUT("ERR-012", 408, "{0}");

    private final String errorCode;
    private final int httpStatus;
    private final String description;

    public String getDescription(Object... formatArguments) {
        return MessageFormat.format(description, formatArguments);
    }
}
