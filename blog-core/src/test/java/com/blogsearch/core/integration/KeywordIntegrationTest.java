package com.blogsearch.core.integration;

import com.blogsearch.core.dto.KeywordCreateDTO;
import com.blogsearch.core.dto.KeywordDetailDTO;
import com.blogsearch.core.service.KeywordMetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("core-test")
public class KeywordIntegrationTest {
    
    @Autowired
    KeywordMetaService keywordMetaService;

    @BeforeEach
    void setUp() {
        KeywordCreateDTO keywordCreateDTO = KeywordCreateDTO.builder().keyword("test5").build();
        keywordMetaService.saveKeyword(keywordCreateDTO);
    }

    @Test
    void 비관적락_동시성_테스트() throws InterruptedException {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        KeywordCreateDTO keywordCreateDTO = KeywordCreateDTO.builder().keyword("test5").build();

        //when
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                keywordMetaService.saveKeyword(keywordCreateDTO);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();

        System.out.println("total elapsed time = " + (end - start));
        List<KeywordDetailDTO> hotKeywords = keywordMetaService.getHotKeywords();
        assertThat(hotKeywords).extracting("searchCount").containsExactly(11L);
    }
}
