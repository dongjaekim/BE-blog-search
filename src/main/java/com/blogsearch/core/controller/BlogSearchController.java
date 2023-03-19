package com.blogsearch.core.controller;

import com.blogsearch.core.dto.KeywordDetailDTO;
import com.blogsearch.core.dto.BlogSearchDetailDTO;
import com.blogsearch.core.service.BlogSearchService;
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
    private final KeywordMetaService keywordService;

    @GetMapping("/blogs")
    public ResponseEntity<BlogSearchDetailDTO> getBlogSearchResults(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) throws InterruptedException {
        return ResponseEntity.ok(blogSearchService.getSearchResults(keyword, page, size, sort));
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordDetailDTO>> getHotKeywords() {
        return ResponseEntity.ok(keywordService.getHotKeywords());
    }
}
