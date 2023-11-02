package com.blogserach.core.service;

import com.blogsearch.core.dto.KeywordMetaCreateDTO;
import com.blogsearch.core.dto.KeywordMetaDetailDTO;
import com.blogsearch.core.model.KeywordMeta;
import com.blogsearch.core.repository.KeywordMetaRepository;
import com.blogsearch.core.service.KeywordMetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KeywordMetaServiceTest {

    @Mock
    private KeywordMetaRepository keywordMetaRepository;

    @InjectMocks
    private KeywordMetaService keywordMetaService;

    private final List<KeywordMeta> keywordMetaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 10; i >= 1; i--) {
            KeywordMeta keywordMeta = KeywordMeta.builder()
                    .keyword("test" + i)
                    .searchCount((long) i)
                    .build();
            keywordMetaList.add(keywordMeta);
        }
    }

    @Test
    void 검색_수_상위_10개_키워드_조회() {
        //given
        String mostSearchedKeyword = "test10";
        long mostSearchedCount = 10;
        given(keywordMetaRepository.findTop10ByOrderBySearchCountDesc())
                .willReturn(keywordMetaList);

        //when
        List<KeywordMetaDetailDTO> hotKeywordList = keywordMetaService.getHotKeywords();

        //then
        assertThat(hotKeywordList.size()).isEqualTo(10);
        assertThat(hotKeywordList.get(0).getKeyword()).isEqualTo(mostSearchedKeyword);
        assertThat(hotKeywordList.get(0).getSearchCount()).isEqualTo(mostSearchedCount);
        then(keywordMetaRepository).should(times(1)).findTop10ByOrderBySearchCountDesc();
    }

    @Test
    void 새로운_키워드_저장() {
        //given
        String keyword = "new keyword";
        KeywordMetaCreateDTO keywordCreateDTO = KeywordMetaCreateDTO.builder()
                .keyword(keyword)
                .build();
        KeywordMeta keywordMeta = KeywordMeta.builder()
                .id(11L)
                .keyword(keyword)
                .searchCount(1L)
                .build();

        given(keywordMetaRepository.findByKeyword(anyString()))
                .willReturn(Optional.empty());
        given(keywordMetaRepository.save(any(KeywordMeta.class)))
                .willReturn(keywordMeta);

        //when
        KeywordMetaDetailDTO keywordDetailDTO = keywordMetaService.saveKeyword(keywordCreateDTO);

        //then
        assertThat(keywordDetailDTO.getId()).isEqualTo(keywordMeta.getId());
        assertThat(keywordDetailDTO.getSearchCount()).isEqualTo(keywordMeta.getSearchCount());
        then(keywordMetaRepository).should(times(1)).findByKeyword(anyString());
        then(keywordMetaRepository).should(times(1)).save(any(KeywordMeta.class));
    }

    @Test
    void 기존_키워드_저장() {
        //given
        String keyword = "test1";
        KeywordMetaCreateDTO keywordCreateDTO = KeywordMetaCreateDTO.builder()
                .keyword(keyword)
                .build();
        KeywordMeta keywordMeta = KeywordMeta.builder()
                .id(1L)
                .keyword(keyword)
                .searchCount(2L)
                .build();

        given(keywordMetaRepository.findByKeyword(anyString()))
                .willReturn(Optional.empty());
        given(keywordMetaRepository.save(any(KeywordMeta.class)))
                .willReturn(keywordMeta);

        //when
        KeywordMetaDetailDTO keywordDetailDTO = keywordMetaService.saveKeyword(keywordCreateDTO);

        //then
        assertThat(keywordDetailDTO.getId()).isEqualTo(keywordMeta.getId());
        assertThat(keywordDetailDTO.getSearchCount()).isEqualTo(keywordMeta.getSearchCount());
        then(keywordMetaRepository).should(times(1)).findByKeyword(anyString());
        then(keywordMetaRepository).should(times(1)).save(any(KeywordMeta.class));
    }
}
