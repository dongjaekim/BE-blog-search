package com.blogsearch.core.service.external;

import com.blogsearch.core.dto.BlogSearchDetailDTO;

public interface ExternalSearchService {

    BlogSearchDetailDTO searchFromExternalSource(String keyword, Integer page, Integer size, String sort) throws InterruptedException;
}
