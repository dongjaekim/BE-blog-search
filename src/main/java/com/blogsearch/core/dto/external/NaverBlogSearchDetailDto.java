package com.blogsearch.core.dto.external;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NaverBlogSearchDetailDto {

    private String title;

    private String link;

    private String description;

    private String bloggername;

    private String bloggerlink;

    private LocalDateTime postdate;
}
