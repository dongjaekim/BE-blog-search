package com.blogsearch.core.service.external;

import com.blogsearch.core.dto.BlogSearchDetailDTO;
import com.blogsearch.core.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.core.dto.external.NaverBlogSearchDetailDTO;
import com.blogsearch.core.utils.mapper.SearchDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class NaverSearchService implements ExternalSearchService {

    private final WebClient webClient;
    private final SearchDetailMapper searchDetailMapper = SearchDetailMapper.INSTANCE;

    @Value("${search.source.naver.host}")
    private String host;

    @Value("${search.source.naver.path}")
    private String path;

    @Value("${search.source.naver.client-id}")
    private String clientId;

    @Value("${search.source.naver.client-secret}")
    private String clientSecret;

    @Override
    public BlogSearchDetailDTO searchFromExternalSource(String keyword, Integer page, Integer size, String sort) throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(1);

        AtomicReference<BlogSearchDetailDTO> output = new AtomicReference<>();
        webClient.get()
                .uri(host, uri -> uri
                        .path(path)
                        .queryParam("query", keyword)
                        .queryParam("start", page * size)
                        .queryParam("display", size)
                        .queryParam("sort", sort)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
                .bodyToMono(NaverBlogSearchDetailDTO.class)
//                .timeout(Duration.ofMillis(1000))
                .doOnTerminate(cdl::countDown)
                .subscribe(response -> {
                    output.set(searchDetailMapper.toBlogSearchDetailDTO(response));
                    System.out.println("output.get() = " + output.get());
                });
        cdl.await();
        return output.get();
    }
}
