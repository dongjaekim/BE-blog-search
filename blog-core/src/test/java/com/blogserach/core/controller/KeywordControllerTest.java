package com.blogserach.core.controller;

import com.blogsearch.core.controller.KeywordMetaController;
import com.blogsearch.core.dto.KeywordMetaDetailDTO;
import com.blogsearch.core.service.KeywordMetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KeywordMetaController.class)
class KeywordControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private KeywordMetaController keywordMetaController;

    @MockBean
    private KeywordMetaService keywordMetaService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(keywordMetaController)
                .build();
    }

    @Test
    void 인기_검색어_조회() throws Exception {
        //given
        List<KeywordMetaDetailDTO> mockResponseDto = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            mockResponseDto.add(
                    KeywordMetaDetailDTO.builder()
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
                        get("/apis/v1/hot-keywords")
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
