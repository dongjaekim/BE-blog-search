package com.blogsearch.core.service;

import com.blogsearch.core.dto.BlogSearchDetailDTO;
import com.blogsearch.core.service.external.ExternalSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogSearchService {

    private final KeywordMetaService keywordService;
    private final ExternalSearchService externalSearchService;

    public BlogSearchDetailDTO getSearchResults(String keyword, Integer page, Integer size, String sort) throws InterruptedException {
        BlogSearchDetailDTO output = externalSearchService.searchFromExternalSource(keyword, page, size, sort);
        keywordService.saveKeyword(keyword);
        return output;
    }

}
