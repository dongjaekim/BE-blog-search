package com.blogsearch.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KeywordDetailDto {

    @Schema(description = "키워드 id")
    private Long id;

    @Schema(description = "키워드")
    private String keyword;

    @Schema(description = "검색 수")
    private Long count;
}
