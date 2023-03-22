package com.blogsearch.service.external;

import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.dto.external.NaverBlogSearchDetailDTO;
import com.blogsearch.utils.client.naver.NaverApiClient;
import com.blogsearch.utils.validator.SortParam;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class NaverBlogSearchServiceTest {

    @Mock
    NaverApiClient naverApiClient;

    @InjectMocks
    NaverBlogSearchService naverBlogSearchService;

    @Test
    void 네이버_블로그_검색_API_호출() {
        //given
        String keyword = "네이버";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        NaverBlogSearchDetailDTO naverBlogSearchDetailDTO = NaverBlogSearchDetailDTO.builder()
                .total(100000)
                .start(1)
                .display(1)
                .items(List.of(
                        NaverBlogSearchDetailDTO.Item.builder()
                                .bloggername("naver blogger")
                                .description("dummy data for test")
                                .title("naver blog test")
                                .bloggerlink("https://naver.test.com/test")
                                .link("https://naver.test.com/test/posts/1")
                                .postdate("20230320")
                                .build()
                ))
                .build();

        given(naverApiClient.searchBlogResults(anyString(), anyString(), anyInt(), anyInt()))
                .willReturn(naverBlogSearchDetailDTO);

        //when
        BlogSearchDetailDTO blogSearchDetailDTO = naverBlogSearchService.searchFromExternalSource(keyword, sort, page, size);

        //then
        assertThat(blogSearchDetailDTO.getTotal_count()).isEqualTo(naverBlogSearchDetailDTO.getTotal());
        assertThat(blogSearchDetailDTO.getBlogPosts()).extracting("blogname").containsExactly(naverBlogSearchDetailDTO.getItems().get(0).getBloggername());
        assertThat(blogSearchDetailDTO.getBlogPosts()).extracting("contents").containsExactly(naverBlogSearchDetailDTO.getItems().get(0).getDescription());
        then(naverApiClient).should(times(1)).searchBlogResults(anyString(), anyString(), anyInt(), anyInt());
    }
}