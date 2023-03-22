package com.blogsearch.service.external;

import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.utils.client.kakao.KakaoApiClient;
import com.blogsearch.utils.mapper.SearchDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
@RequiredArgsConstructor
public class KakaoBlogSearchService implements ExternalBlogSearchService {

    private final KakaoApiClient kakaoApiClient;
    private final SearchDetailMapper searchDetailMapper = SearchDetailMapper.INSTANCE;

    @Override
    public BlogSearchDetailDTO searchFromExternalSource(String keyword, String sort, Integer page, Integer size) {
        return searchDetailMapper.toBlogSearchDetailDTO(kakaoApiClient.searchBlogResults(keyword, sort, page, size));
    }
}
