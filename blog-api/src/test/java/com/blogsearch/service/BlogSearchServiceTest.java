package com.blogsearch.service;

import com.blogsearch.core.dto.KeywordMetaCreateDTO;
import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.dto.external.NaverBlogSearchDetailDTO;
import com.blogsearch.global.exception.BlogSearchException;
import com.blogsearch.global.exception.BlogSearchExceptionType;
import com.blogsearch.service.external.ExternalBlogSearchService;
import com.blogsearch.service.external.KakaoBlogSearchService;
import com.blogsearch.service.external.NaverBlogSearchService;
import com.blogsearch.utils.mapper.SearchDetailMapper;
import com.blogsearch.utils.validator.SortParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BlogSearchServiceTest {

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Mock
    KakaoBlogSearchService kakaoBlogSearchService;

    @Mock
    NaverBlogSearchService naverBlogSearchService;

    List<ExternalBlogSearchService> externalBlogSearchServiceList;

    private BlogSearchService blogSearchService;

    private final SearchDetailMapper searchDetailMapper = SearchDetailMapper.INSTANCE;
    private KakaoBlogSearchDetailDTO kakaoBlogSearchDetailDTO;
    private NaverBlogSearchDetailDTO naverBlogSearchDetailDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        externalBlogSearchServiceList = new ArrayList<>();
        externalBlogSearchServiceList.add(kakaoBlogSearchService);
        externalBlogSearchServiceList.add(naverBlogSearchService);

        kakaoBlogSearchDetailDTO = KakaoBlogSearchDetailDTO.builder()
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

        naverBlogSearchDetailDTO = NaverBlogSearchDetailDTO.builder()
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

        blogSearchService = new BlogSearchService(applicationEventPublisher, externalBlogSearchServiceList);
    }

    @Test
    void 검색_결과_카카오_API로_가져오기_성공() {
        //given
        String keyword = "카카오";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        given(kakaoBlogSearchService.searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt()))
                .willReturn(searchDetailMapper.toBlogSearchDetailDTO(kakaoBlogSearchDetailDTO));

        //when
        BlogSearchDetailDTO searchResults = blogSearchService.getSearchResults(keyword, sort, page, size);

        //then
        assertThat(searchResults.getTotal_count()).isEqualTo(kakaoBlogSearchDetailDTO.getMeta().getTotal_count());
        assertThat(searchResults.getBlogPosts()).extracting("blogname").containsExactly(kakaoBlogSearchDetailDTO.getDocuments().get(0).getBlogname());
        assertThat(searchResults.getBlogPosts()).extracting("contents").containsExactly(kakaoBlogSearchDetailDTO.getDocuments().get(0).getContents());
        then(kakaoBlogSearchService).should(times(1)).searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt());
        then(applicationEventPublisher).should(times(1)).publishEvent(any(KeywordMetaCreateDTO.class));
    }

    @Test
    void 검색_결과_카카오_API로_가져오기_실패_네이버_API로_가져오기_성공() {
        String keyword = "카카오";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        given(kakaoBlogSearchService.searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt()))
                .willThrow(new BlogSearchException(BlogSearchExceptionType.READ_TIME_OUT));

        given(naverBlogSearchService.searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt()))
                .willReturn(searchDetailMapper.toBlogSearchDetailDTO(naverBlogSearchDetailDTO));

        //when
        BlogSearchDetailDTO searchResults = blogSearchService.getSearchResults(keyword, sort, page, size);

        //then
        assertThat(searchResults.getTotal_count()).isEqualTo(naverBlogSearchDetailDTO.getTotal());
        assertThat(searchResults.getBlogPosts()).extracting("blogname").containsExactly(naverBlogSearchDetailDTO.getItems().get(0).getBloggername());
        assertThat(searchResults.getBlogPosts()).extracting("contents").containsExactly(naverBlogSearchDetailDTO.getItems().get(0).getDescription());
        then(kakaoBlogSearchService).should(times(1)).searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt());
        then(naverBlogSearchService).should(times(1)).searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt());
        then(applicationEventPublisher).should(times(1)).publishEvent(any(KeywordMetaCreateDTO.class));
    }

    @Test
    void 검색_결과_카카오_네이버_API로_가져오기_모두_실패() {
        String keyword = "카카오";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        given(kakaoBlogSearchService.searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt()))
                .willThrow(new BlogSearchException(BlogSearchExceptionType.READ_TIME_OUT));

        given(naverBlogSearchService.searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt()))
                .willThrow(new BlogSearchException(BlogSearchExceptionType.READ_TIME_OUT));

        //when

        //then
        assertThatThrownBy(() -> blogSearchService.getSearchResults(keyword, sort, page, size))
                .isInstanceOf(BlogSearchException.class);
        then(kakaoBlogSearchService).should(times(1)).searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt());
        then(naverBlogSearchService).should(times(1)).searchFromExternalSource(anyString(), anyString(), anyInt(), anyInt());
        then(applicationEventPublisher).should(times(0)).publishEvent(any(KeywordMetaCreateDTO.class));
    }

}