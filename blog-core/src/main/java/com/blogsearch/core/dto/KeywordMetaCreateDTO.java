package com.blogsearch.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KeywordMetaCreateDTO {

    @Schema(description = "키워드")
    private String keyword;
}
