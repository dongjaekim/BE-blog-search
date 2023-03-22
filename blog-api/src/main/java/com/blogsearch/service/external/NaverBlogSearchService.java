package com.blogsearch.service.external;

import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.utils.validator.SortParam;
import com.blogsearch.utils.client.naver.NaverApiClient;
import com.blogsearch.utils.mapper.SearchDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@RequiredArgsConstructor
public class NaverBlogSearchService implements ExternalBlogSearchService {

    private final NaverApiClient naverApiClient;
    private final SearchDetailMapper searchDetailMapper = SearchDetailMapper.INSTANCE;

    @Override
    public BlogSearchDetailDTO searchFromExternalSource(String keyword, String sort, Integer page, Integer size) {
        return searchDetailMapper.toBlogSearchDetailDTO(naverApiClient.searchBlogResults(keyword, SortParam.KAKAO.mapSortValue(sort), (page - 1) * size + 1, size));
    }
}
