package com.blogsearch.core.service;

import com.blogsearch.core.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.core.service.external.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogSearchService {

    private final KeywordMetaService keywordService;
    private final ExternalApiClient externalSearchService;

    public Mono<KakaoBlogSearchDetailDTO> getSearchResults(String keyword, Integer page, Integer size, String sort) throws InterruptedException {
        Mono<KakaoBlogSearchDetailDTO> output = externalSearchService.searchFromExternalSource(keyword, page, size, sort);
        keywordService.saveKeyword(keyword);
        return output;
    }

}
