package com.blogsearch.controller;

import com.blogsearch.core.dto.KeywordDetailDTO;
import com.blogsearch.dto.BlogSearchDetailDTO;
import com.blogsearch.service.BlogSearchService;
import com.blogsearch.core.service.KeywordMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apis/v1/search")
@RequiredArgsConstructor
public class BlogSearchController {

    private final BlogSearchService blogSearchService;
    private final KeywordMetaService keywordMetaService;

    @GetMapping("/blogs")
    public ResponseEntity<BlogSearchDetailDTO> getBlogSearchResults(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "accuracy") String sort,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(blogSearchService.getSearchResults(keyword, sort, page, size));
    }

    @GetMapping("/hot-keywords")
    public ResponseEntity<List<KeywordDetailDTO>> getHotKeywords() {
        return ResponseEntity.ok(keywordMetaService.getHotKeywords());
    }
}
