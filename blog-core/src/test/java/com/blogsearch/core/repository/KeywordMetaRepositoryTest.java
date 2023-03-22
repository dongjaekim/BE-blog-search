package com.blogsearch.core.repository;

import com.blogsearch.core.model.KeywordMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class KeywordMetaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    KeywordMetaRepository keywordMetaRepository;

    @BeforeEach
    void setUp() {

        List<KeywordMeta> keywordMetaList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            KeywordMeta keywordMeta = KeywordMeta.builder()
                    .keyword("test" + i)
                    .searchCount((long) i)
                    .build();
            keywordMetaList.add(keywordMeta);
        }
        keywordMetaRepository.saveAll(keywordMetaList);

        em.flush();
        em.clear();
    }

    @Test
    void 검색_수_상위_10개_키워드_조회() {
        //given
        String mostSearchedKeyword = "test10";
        long mostSearchedCount = 10;

        //when
        List<KeywordMeta> hotKeywordList = keywordMetaRepository.findTop10ByOrderBySearchCountDesc();

        //then
        assertThat(hotKeywordList.size()).isEqualTo(10);
        assertThat(hotKeywordList.get(0).getKeyword()).isEqualTo(mostSearchedKeyword);
        assertThat(hotKeywordList.get(0).getSearchCount()).isEqualTo(mostSearchedCount);
    }

    @Test
    void 키워드_조회() {
        //given
        String keyword = "test5";

        //when
        Optional<KeywordMeta> keywordMeta = keywordMetaRepository.findByKeyword(keyword);

        //then
        assertThat(keywordMeta.isPresent()).isTrue();
        assertThat(keywordMeta.get().getSearchCount()).isEqualTo(5);
    }
}