package com.blogsearch.core.service.external;

import com.blogsearch.core.dto.external.KakaoBlogSearchDetailDTO;
import reactor.core.publisher.Mono;

public interface ExternalApiClient {

    Mono<KakaoBlogSearchDetailDTO> searchFromExternalSource(String keyword, Integer page, Integer size, String sort) throws InterruptedException;
}
