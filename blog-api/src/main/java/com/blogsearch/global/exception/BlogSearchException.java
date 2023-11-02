package com.blogsearch.global.exception;

import lombok.Getter;

@Getter
public class BlogSearchException extends RuntimeException {

    private final BlogSearchExceptionType blogSearchExceptionType;

    private Object[] formatArguments;

    public BlogSearchException(BlogSearchExceptionType blogSearchExceptionType) {
        super(blogSearchExceptionType.getDescription());
        this.blogSearchExceptionType = blogSearchExceptionType;
    }

    public BlogSearchException(BlogSearchExceptionType blogSearchExceptionType, Object... formatArguments) {
        super(blogSearchExceptionType.getDescription());
        this.blogSearchExceptionType = blogSearchExceptionType;
        this.formatArguments = formatArguments;
    }
}