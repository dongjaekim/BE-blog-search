package com.blogsearch.service.external;

import com.blogsearch.dto.BlogSearchDetailDTO;
import reactor.core.publisher.Mono;

public interface ExternalBlogSearchService {

    Mono<BlogSearchDetailDTO> searchFromExternalSource(String keyword, String sort, Integer page, Integer size) throws InterruptedException;
}
