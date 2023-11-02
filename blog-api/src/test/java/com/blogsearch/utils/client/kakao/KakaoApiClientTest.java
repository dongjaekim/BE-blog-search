/*
package com.blogsearch.utils.client.kakao;

import com.blogsearch.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.utils.validator.SortParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@AutoConfigureWireMock(port = 8081)
class KakaoApiClientTest {

    @Autowired
    KakaoApiClient kakaoApiClient;

    @LocalServerPort
    private int port;

    private ObjectMapper objectMapper;
    private KakaoBlogSearchDetailDTO kakaoBlogSearchDetailDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
    }

    @Test
    void 정상_API_호출() throws JsonProcessingException {
        //given
        String keyword = "카카오";
        String sort = SortParam.KAKAO.getSortAccuracy();
        Integer page = 1;
        Integer size = 1;

        String jsonBody = objectMapper.writeValueAsString(kakaoBlogSearchDetailDTO);
        stubFor(get(urlEqualTo("/v2/search/blog"))
                .willReturn(aResponse()
                        .withBody(jsonBody)));

        //when
        KakaoBlogSearchDetailDTO result = kakaoApiClient.searchBlogResults(keyword, sort, page, size);
        Assertions.assertEquals(jsonBody, objectMapper.writeValueAsString(result));
    }

}
*/
