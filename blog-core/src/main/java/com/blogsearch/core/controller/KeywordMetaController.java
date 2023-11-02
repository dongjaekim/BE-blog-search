package com.blogsearch.core.controller;

import com.blogsearch.core.dto.KeywordMetaDetailDTO;
import com.blogsearch.core.service.KeywordMetaService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apis/v1/hot-keywords")
@RequiredArgsConstructor
public class KeywordMetaController {

    private final KeywordMetaService keywordMetaService;

    @ApiOperation(value = "인기 검색어 목록 조회",
            notes = "**인기 검색어 상위 10개를 조회해오는 기능 제공**\n\n" +
                    "- 많이 검색된 검색 순으로 정렬되어 결과 제공\n\n" +
                    "- 상위 10개를 결과로 보여주며 10개보다 결과가 적을 시 조회된 결과만큼 보여줌"
    )
    @GetMapping()
    public ResponseEntity<List<KeywordMetaDetailDTO>> getHotKeywords() {
        return ResponseEntity.ok(keywordMetaService.getHotKeywords());
    }
}
