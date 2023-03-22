package com.blogsearch.service.external;

import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.utils.client.kakao.KakaoApiClient;
import com.blogsearch.utils.validator.SortParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoBlogSearchServiceTest {

    @Mock
    KakaoApiClient kakaoApiClient;

    @InjectMocks
    KakaoBlogSearchService kakaoBlogSearchService;

    @Test
    void 카카오_블로그_검색_API_호출() {
        //given
        String keyword = "카카오";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        KakaoBlogSearchDetailDTO kakaoBlogSearchDetailDTO = KakaoBlogSearchDetailDTO.builder()
                .meta(
                        KakaoBlogSearchDetailDTO.Meta.builder()
                                .total_count(100000)
                                .pageable_count(800)
                                .is_end(false)
                                .build()
                )
                .documents(List.of(
                        KakaoBlogSearchDetailDTO.Document.builder()
                                .blogname("kakao blogger")
                                .contents("dummy data for test")
                                .title("kakao blog test")
                                .url("https://kakao.test.com/test")
                                .thumbnail("https://kakao.test.com/test.jpg")
                                .datetime(LocalDateTime.now())
                                .build()
                ))
                .build();

        given(kakaoApiClient.searchBlogResults(anyString(), anyString(), anyInt(), anyInt()))
                .willReturn(kakaoBlogSearchDetailDTO);

        //when
        BlogSearchDetailDTO blogSearchDetailDTO = kakaoBlogSearchService.searchFromExternalSource(keyword, sort, page, size);

        //then
        assertThat(blogSearchDetailDTO.getTotal_count()).isEqualTo(kakaoBlogSearchDetailDTO.getMeta().getTotal_count());
        assertThat(blogSearchDetailDTO.getBlogPosts()).extracting("blogname").containsExactly(kakaoBlogSearchDetailDTO.getDocuments().get(0).getBlogname());
        assertThat(blogSearchDetailDTO.getBlogPosts()).extracting("contents").containsExactly(kakaoBlogSearchDetailDTO.getDocuments().get(0).getContents());
        then(kakaoApiClient).should(times(1)).searchBlogResults(anyString(), anyString(), anyInt(), anyInt());
    }
}