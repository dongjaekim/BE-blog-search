package com.blogsearch.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BlogSearchDetailDto {

    @Schema(description = "블로그 이름")
    private String blogname;

    @Schema(description = "글 제목")
    private String title;

    @Schema(description = "글 내용")
    private String contents;

    @Schema(description = "글 개시 시간")
    private LocalDateTime datetime;

    @Schema(description = "글 url")
    private String url;

}
