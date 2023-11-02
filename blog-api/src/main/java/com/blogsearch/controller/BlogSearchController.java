package com.blogsearch.controller;

import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.service.BlogSearchService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public Mono<BlogSearchDetailDTO> getBlogSearchResults(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) throws InterruptedException {
//        return ResponseEntity.ok(blogSearchService.getSearchResults(keyword, page, size, sort));
        return blogSearchService.getSearchResults(keyword, sort, page, size);
    }

    @GetMapping("/test")
    public String test() {
        return "ok";
    }
}
