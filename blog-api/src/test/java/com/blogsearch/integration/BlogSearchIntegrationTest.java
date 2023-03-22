package com.blogsearch.integration;

import com.blogsearch.controller.BlogSearchController;
import com.blogsearch.core.dto.KeywordMetaCreateDTO;
import com.blogsearch.core.dto.KeywordMetaDetailDTO;
import com.blogsearch.core.service.KeywordMetaService;
import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.global.exception.BlogSearchExceptionHandler;
import com.blogsearch.service.BlogSearchService;
import com.blogsearch.utils.validator.SortParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BlogSearchIntegrationTest {

    @Autowired
    BlogSearchController blogSearchController;

    @Autowired
    BlogSearchService blogSearchService;

    @Autowired
    KeywordMetaService keywordMetaService;

    @Test
    void 검색_성공() {
        //given
        String keyword = "kakao";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 10;

        //when
        ResponseEntity<BlogSearchDetailDTO> blogSearchResults = blogSearchController.getBlogSearchResults(keyword, sort, page, size);

        //then
        assertThat(blogSearchResults.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(blogSearchResults.getBody().getTotal_count()).isGreaterThan(10);
    }

    @Test
    void 검색_20번_및_키워드_메타데이터_확인() throws InterruptedException {
        //given
        String keyword = "kakao";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 5;
        Integer size = 10;

        keywordMetaService.saveKeyword(KeywordMetaCreateDTO.builder()
                .keyword(keyword)
                .build());

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch countDownLatch = new CountDownLatch(20);

        //when
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                blogSearchController.getBlogSearchResults(keyword, sort, page, size);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        List<KeywordMetaDetailDTO> hotKeywords = keywordMetaService.getHotKeywords();

        //then
        assertThat(hotKeywords.size()).isEqualTo(1);
        assertThat(hotKeywords.get(0).getSearchCount()).isEqualTo(21);
    }
}
