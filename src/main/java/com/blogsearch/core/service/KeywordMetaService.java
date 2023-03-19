package com.blogsearch.core.service;

import com.blogsearch.core.dto.KeywordDetailDTO;
import com.blogsearch.core.model.KeywordMeta;
import com.blogsearch.core.repository.KeywordMetaRepository;
import com.blogsearch.core.utils.mapper.KeywordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordMetaService {

    private final KeywordMetaRepository keywordRepository;
    private final KeywordMapper keywordMapper = KeywordMapper.INSTANCE;

    public List<KeywordDetailDTO> getHotKeywords() {
        return keywordMapper.toDtoList(keywordRepository.findTop10ByOrderBySearchCountDesc());
    }

    public KeywordDetailDTO saveKeyword(String keyword) {
        KeywordMeta keywordMeta = keywordRepository.findByKeyword(keyword)
                .orElse(KeywordMeta
                        .builder()
                        .keyword(keyword)
                        .build());
        keywordMeta.increaseSearchCount();

        return keywordMapper.toDto(keywordRepository.save(keywordMeta));
    }
}
