package com.blogsearch.controller;

import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.service.BlogSearchService;
import com.blogsearch.utils.validator.SortValid;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/apis/v1/search")
@RequiredArgsConstructor
public class BlogSearchController {

    private final BlogSearchService blogSearchService;

    @ApiOperation(value = "블로그 검색",
            notes = "**주어진 키워드를 기반으로 검색하며 페이징 기능을 제공**\n\n" +
                    "- querystring keyword: 필수값이며 검색할 키워드를 입력 받음\n\n" +
                    "- querystring sort: 선택값이며 정렬 기준을 입력 받음. 값이 없을 시 정확도 기반으로 세팅\n\n" +
                    "\t- sort 값은 accuracy(정확도), recency(날짜순)만 허용하며 이외의 값은 400 에러\n\n" +
                    "- querystring page: 선택값이며 가져올 페이지 번호를 입력 받음. 값이 없을 시 1로 세팅\n\n" +
                    "\t - page 값은 1 ~ 50 사이의 값만 허용, 이외의 값은 400 에러\n\n" +
                    "- querystring size: 선택값이며 한 번에 가져올 검색 결과 수를 입력 받음. 값이 없을 시 10으로 세팅\n\n" +
                    "\t - size 값은 1 ~ 50 사이의 값만 허용, 이외의 값은 400 에러\n\n" +
                    "- 요청 시간이 오래 걸려 결과를 못가지고 오면 408 에러"
    )
    @GetMapping("/blogs")
    public ResponseEntity<BlogSearchDetailDTO> getBlogSearchResults(
            @ApiParam(value = "검색할 키워드")
            @RequestParam String keyword,
            @ApiParam(value = "정렬 기준")
            @RequestParam(required = false, defaultValue = "accuracy") @SortValid String sort,
            @ApiParam(value = "가져올 페이지 번호")
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "should be greater than or equal to 1") @Max(value = 50, message = "should be less than or equal to 50") Integer page,
            @ApiParam(value = "한 번에 가져올 검색 결과 수")
            @RequestParam(required = false, defaultValue = "10") @Min(value = 1, message = "should be greater than or equal to 1") @Max(value = 50, message = "should be less than or equal to 50") Integer size
    ) {
        return ResponseEntity.ok(blogSearchService.getSearchResults(keyword, sort, page, size));
    }

}
