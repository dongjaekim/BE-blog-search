package com.blogsearch.service.external;

import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.utils.mapper.SearchDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Order(1)
@Component
@RequiredArgsConstructor
public class KakaoBlogSearchService implements ExternalBlogSearchService {

    private final WebClient webClient;
    private final SearchDetailMapper searchDetailMapper = SearchDetailMapper.INSTANCE;

    @Value("${search.source.kakao.host}")
    private String host;

    @Value("${search.source.kakao.path}")
    private String path;

    @Value("${search.source.kakao.api-key}")
    private String apiKey;

    @Override
    public Mono<BlogSearchDetailDTO> searchFromExternalSource(String keyword, String sort, Integer page, Integer size) throws InterruptedException {
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
                .bodyToMono(KakaoBlogSearchDetailDTO.class)
                .map(searchDetailMapper::toBlogSearchDetailDTO);
//                .timeout(Duration.ofMillis(1000));
//                .doOnTerminate(cdl::countDown)
//                .subscribe(response -> output.set(searchDetailMapper.toBlogSearchDetailDTO(response)));
//        cdl.await();
//        return output.get();
    }
}
