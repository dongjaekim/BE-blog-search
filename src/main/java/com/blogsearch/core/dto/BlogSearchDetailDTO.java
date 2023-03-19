package com.blogsearch.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class BlogSearchDetailDTO {

    @Schema(description = "검색된 문서 수")
    private Integer total_count;

    @Schema(description = "검색 결과")
    private List<Content> contents;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Content {
        @Schema(description = "블로그 이름")
        private String blogname;

        @Schema(description = "글 제목")
        private String title;

        @Schema(description = "글 내용")
        private String contents;

        @Schema(description = "글 개시 시간")
        private String datetime;

        @Schema(description = "글 url")
        private String url;
    }
}
