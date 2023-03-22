package com.blogsearch.utils.client.naver;

import com.blogsearch.utils.validator.SortParam;
import com.blogsearch.utils.validator.SortValid;
import com.blogsearch.dto.external.NaverBlogSearchDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@FeignClient(name = "naver-api",
        url = "${search.source.naver.host}",
        configuration = NaverApiConfig.class)
public interface NaverApiClient {

    @GetMapping(value = "${search.source.naver.path}",
            produces = "application/json")
    NaverBlogSearchDetailDTO searchBlogResults(
            @RequestParam(name = "query") String query,
            @RequestParam(required = false, name = "sort") @SortValid(source = SortParam.NAVER) String sort,
            @RequestParam(required = false, name = "start") @Min(value = 1, message = "should be greater than or equal to 1") @Max(value = 1000, message = "should be less than or equal to 1000") Integer start,
            @RequestParam(required = false, name = "display") @Min(value = 1, message = "should be greater than or equal to 1") @Max(value = 100, message = "should be less than or equal to 100") Integer display);
}
