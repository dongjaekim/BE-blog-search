package com.blogsearch.service.external;

import com.blogsearch.dto.BlogSearchDetailDTO;

public interface ExternalBlogSearchService {

    BlogSearchDetailDTO searchFromExternalSource(String keyword, String sort, Integer page, Integer size);
}
