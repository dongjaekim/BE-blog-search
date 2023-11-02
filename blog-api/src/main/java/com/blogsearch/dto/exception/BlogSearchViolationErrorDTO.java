package com.blogsearch.dto.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlogSearchViolationErrorDTO {
    private final String fieldName;
    private final String detailMessage;
}
