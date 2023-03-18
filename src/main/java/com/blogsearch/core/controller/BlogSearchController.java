package com.blogsearch.core.controller;

import com.blogsearch.core.dto.KeywordDetailDto;
import com.blogsearch.core.dto.BlogSearchDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apis/v1/search")
@RequiredArgsConstructor
public class BlogSearchController {

    @GetMapping("/blogs")
    public ResponseEntity<Page<BlogSearchDetailDto>> getBlogSearchResults() {
        return ResponseEntity.of(null);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordDetailDto>> getHotKeywords() {
        return ResponseEntity.of(null);
    }
}
