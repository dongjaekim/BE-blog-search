package com.blogsearch.global.exception;

import com.blogsearch.dto.exception.BlogSearchErrorDTO;
import com.blogsearch.dto.exception.BlogSearchViolationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.stream.Collectors;

@RestControllerAdvice(annotations = RestController.class)
public class BlogSearchExceptionHandler {

    @ExceptionHandler(BlogSearchException.class)
    public ResponseEntity<BlogSearchErrorDTO> blogSearchExceptionHandler(BlogSearchException e) {
        Object[] formatArguments = e.getFormatArguments();
        String description = e.getBlogSearchExceptionType().getDescription();
        if (formatArguments != null) {
            description = MessageFormat.format(description, formatArguments);
        }

        BlogSearchErrorDTO blogSearchErrorDTO = BlogSearchErrorDTO.builder()
                .errorCode(e.getBlogSearchExceptionType().getErrorCode())
                .httpStatus(e.getBlogSearchExceptionType().getHttpStatus())
                .message(description)
                .build();

        return new ResponseEntity<>(blogSearchErrorDTO, HttpStatus.valueOf(blogSearchErrorDTO.getHttpStatus()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BlogSearchErrorDTO> requestParamConstraintViolationExceptionHandler(ConstraintViolationException e) {
        BlogSearchErrorDTO blogSearchErrorDTO = BlogSearchErrorDTO.builder()
                .errorCode(BlogSearchExceptionType.CONSTRAINT_VIOLATION.getErrorCode())
                .httpStatus(BlogSearchExceptionType.CONSTRAINT_VIOLATION.getHttpStatus())
                .message(BlogSearchExceptionType.CONSTRAINT_VIOLATION.getDescription())
                .violations(
                        e.getConstraintViolations()
                                .stream()
                                .map(fieldError -> {
                                    String path = fieldError.getPropertyPath().toString();
                                    path = path.substring(path.lastIndexOf(".") + 1);
                                    String field;
                                    switch (path) {
                                        case "arg2":
                                            field = "page";
                                            break;
                                        case "arg3":
                                            field = "size";
                                            break;
                                        default:
                                            field = "sort";
                                            break;
                                    }
                                    String message = fieldError.getMessage();
                                    return new BlogSearchViolationErrorDTO(field, message);
                                })
                                .collect(Collectors.toList())
                )
                .build();
        return new ResponseEntity<>(blogSearchErrorDTO, HttpStatus.valueOf(blogSearchErrorDTO.getHttpStatus()));
    }

    @ExceptionHandler(SocketException.class)
    public ResponseEntity<BlogSearchErrorDTO> SocketExceptionHandler(SocketException e) {
        BlogSearchErrorDTO blogSearchErrorDTO = BlogSearchErrorDTO.builder()
                .errorCode(BlogSearchExceptionType.INVALID_REQUEST.getErrorCode())
                .httpStatus(BlogSearchExceptionType.INVALID_REQUEST.getHttpStatus())
                .message(BlogSearchExceptionType.INVALID_REQUEST.getDescription())
                .violations(Collections.emptyList())
                .build();

        return new ResponseEntity<>(blogSearchErrorDTO, HttpStatus.valueOf(blogSearchErrorDTO.getHttpStatus()));
    }
}
