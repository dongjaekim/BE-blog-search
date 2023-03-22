package com.blogsearch.utils.client.kakao;

import com.blogsearch.utils.validator.SortValid;
import com.blogsearch.dto.external.KakaoBlogSearchDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@FeignClient(name = "kakao-api",
        url = "${search.source.kakao.host}",
        configuration = KakaoApiConfig.class)
public interface KakaoApiClient {

    @GetMapping(value = "${search.source.kakao.path}",
            produces = "application/json")
    KakaoBlogSearchDetailDTO searchBlogResults(
            @RequestParam(name = "query") String query,
            @RequestParam(required = false, name = "sort") @SortValid String sort,
            @RequestParam(required = false, name = "page") @Min(value = 1, message = "should be greater than or equal to 1") @Max(value = 50, message = "should be less than or equal to 50") Integer page,
            @RequestParam(required = false, name = "size") @Min(value = 1, message = "should be greater than or equal to 1") @Max(value = 50, message = "should be less than or equal to 50") Integer size);

}
