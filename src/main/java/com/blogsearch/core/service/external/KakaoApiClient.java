package com.blogsearch.core.service.external;

import com.blogsearch.core.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.core.utils.mapper.SearchDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class KakaoApiClient implements ExternalApiClient {

    private final WebClient webClient;
    private final SearchDetailMapper searchDetailMapper = SearchDetailMapper.INSTANCE;

    @Value("${search.source.kakao.host}")
    private String host;

    @Value("${search.source.kakao.path}")
    private String path;

    @Value("${search.source.kakao.rest-api-key}")
    private String apiKey;

    @Override
    public Mono<KakaoBlogSearchDetailDTO> searchFromExternalSource(String keyword, Integer page, Integer size, String sort) throws InterruptedException {
//        CountDownLatch cdl = new CountDownLatch(1);

//        AtomicReference<BlogSearchDetailDTO> output = new AtomicReference<>();
        return webClient.get()
                .uri(host, uri -> uri
                        .path(path)
                        .queryParam("query", keyword)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("sort", sort)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", apiKey)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
                .bodyToMono(KakaoBlogSearchDetailDTO.class);
//                .timeout(Duration.ofMillis(1000));
//                .doOnTerminate(cdl::countDown)
//                .subscribe(response -> output.set(searchDetailMapper.toBlogSearchDetailDTO(response)));
//        cdl.await();
//        return output.get();
    }
}
