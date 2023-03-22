package com.blogsearch.dto.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BlogSearchErrorDTO {
    private int httpStatus;
    private String errorCode;
    private String message;
    private List<BlogSearchViolationErrorDTO> violations;
}
