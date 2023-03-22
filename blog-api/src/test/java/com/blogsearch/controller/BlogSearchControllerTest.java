package com.blogsearch.controller;

import com.blogsearch.core.dto.KeywordDetailDTO;
import com.blogsearch.core.service.KeywordMetaService;
import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.dto.external.NaverBlogSearchDetailDTO;
import com.blogsearch.global.exception.BlogSearchExceptionHandler;
import com.blogsearch.service.BlogSearchService;
import com.blogsearch.utils.mapper.SearchDetailMapper;
import com.blogsearch.utils.validator.SortParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private BlogSearchController blogSearchController;

    @MockBean
    private BlogSearchService blogSearchService;

    @MockBean
    private KeywordMetaService keywordMetaService;

    private final SearchDetailMapper searchDetailMapper = SearchDetailMapper.INSTANCE;
    private KakaoBlogSearchDetailDTO kakaoBlogSearchDetailDTO;
    private NaverBlogSearchDetailDTO naverBlogSearchDetailDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(blogSearchController)
                .setControllerAdvice(BlogSearchExceptionHandler.class)
                .build();

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
    }

    @Test
    void 블로그_검색_카카오_API로_가져오기_성공() throws Exception {
        //given
        String keyword = "카카오";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        MultiValueMap<String, String> mockParams = new LinkedMultiValueMap<>();
        mockParams.put("keyword", Collections.singletonList(keyword));
        mockParams.put("sort", Collections.singletonList(sort));
        mockParams.put("page", Collections.singletonList("1"));
        mockParams.put("size", Collections.singletonList("1"));

        BlogSearchDetailDTO mockResponseDTO = searchDetailMapper.toBlogSearchDetailDTO(kakaoBlogSearchDetailDTO);
        given(blogSearchService.getSearchResults(anyString(), anyString(), anyInt(), anyInt()))
                .willReturn(mockResponseDTO);

        //when, then
        mockMvc.perform(
                        get("/apis/v1/search/blogs")
                                .params(mockParams)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_count").value(kakaoBlogSearchDetailDTO.getMeta().getTotal_count()))
                .andExpect(jsonPath("$.blogPosts[0]['blogname']").value(kakaoBlogSearchDetailDTO.getDocuments().get(0).getBlogname()))
                .andExpect(jsonPath("$.blogPosts[0]['contents']").value(kakaoBlogSearchDetailDTO.getDocuments().get(0).getContents()));

        then(blogSearchService).should(times(1)).getSearchResults(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void 블로그_검색_카카오_API로_실패_네이버_API로_가져오기_성공() throws Exception {
        //given
        String keyword = "카카오";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        MultiValueMap<String, String> mockParams = new LinkedMultiValueMap<>();
        mockParams.put("keyword", Collections.singletonList(keyword));
        mockParams.put("sort", Collections.singletonList(sort));
        mockParams.put("page", Collections.singletonList("1"));
        mockParams.put("size", Collections.singletonList("1"));

        BlogSearchDetailDTO mockResponseDTO = searchDetailMapper.toBlogSearchDetailDTO(naverBlogSearchDetailDTO);
        given(blogSearchService.getSearchResults(anyString(), anyString(), anyInt(), anyInt()))
                .willReturn(mockResponseDTO);

        //when, then
        mockMvc.perform(
                        get("/apis/v1/search/blogs")
                                .params(mockParams)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_count").value(naverBlogSearchDetailDTO.getTotal()))
                .andExpect(jsonPath("$.blogPosts[0]['blogname']").value(naverBlogSearchDetailDTO.getItems().get(0).getBloggername()))
                .andExpect(jsonPath("$.blogPosts[0]['contents']").value(naverBlogSearchDetailDTO.getItems().get(0).getDescription()));

        then(blogSearchService).should(times(1)).getSearchResults(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void 블로그_검색_필수_파라미터_누락() throws Exception {
        //given
        String keyword = null;
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        MultiValueMap<String, String> mockParams = new LinkedMultiValueMap<>();
        mockParams.put("keyword", Collections.singletonList(keyword));
        mockParams.put("sort", Collections.singletonList(sort));
        mockParams.put("page", Collections.singletonList("1"));
        mockParams.put("size", Collections.singletonList("1"));

        //when, then
        mockMvc.perform(
                        get("/apis/v1/search/blogs")
                                .params(mockParams)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void 인기_검색어_조회() throws Exception {
        //given
        List<KeywordDetailDTO> mockResponseDto = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            mockResponseDto.add(
                    KeywordDetailDTO.builder()
                            .id((long) i)
                            .keyword("test" + i)
                            .searchCount(i * 10L)
                            .build()
            );
        }

        given(keywordMetaService.getHotKeywords())
                .willReturn(mockResponseDto);

        //when, then
        mockMvc.perform(
                        get("/apis/v1/search/hot-keywords")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['searchCount']").value(100))
                .andExpect(jsonPath("$[5]['searchCount']").value(50))
                .andExpect(jsonPath("$[9]['searchCount']").value(10));

        then(keywordMetaService).should(times(1)).getHotKeywords();
    }

}