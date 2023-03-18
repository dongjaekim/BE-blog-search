package com.blogsearch.core.dto.external;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KakaoBlogSearchDetailDto {

    private String blogname;

    private String contents;

    private LocalDateTime datetime;

    private String thumbnail;

    private String title;

    private String url;
}
