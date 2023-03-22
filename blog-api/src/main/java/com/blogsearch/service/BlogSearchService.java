package com.blogsearch.service;

import com.blogsearch.core.dto.KeywordMetaCreateDTO;
import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.service.external.ExternalBlogSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final List<ExternalBlogSearchService> externalBlogSearchServiceList;

    public BlogSearchDetailDTO getSearchResults(String keyword, String sort, Integer page, Integer size) {
        BlogSearchDetailDTO output = null;

        Iterator<ExternalBlogSearchService> iterator = externalBlogSearchServiceList.iterator();
        while (iterator.hasNext()) {
            ExternalBlogSearchService externalBlogSearchService = iterator.next();
            try {
                output = externalBlogSearchService.searchFromExternalSource(keyword, sort, page, size);
                break;
            } catch (Exception e) {
                if (!iterator.hasNext())
                  throw e;
            }
        }

        applicationEventPublisher.publishEvent(KeywordMetaCreateDTO.builder()
                .keyword(keyword)
                .build()
        );
        return output;
    }
}
