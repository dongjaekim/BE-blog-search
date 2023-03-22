package com.blogsearch.core.service;

import com.blogsearch.core.dto.KeywordMetaCreateDTO;
import com.blogsearch.core.dto.KeywordMetaDetailDTO;
import com.blogsearch.core.utils.mapper.KeywordMetaMapper;
import com.blogsearch.core.model.KeywordMeta;
import com.blogsearch.core.repository.KeywordMetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordMetaService {

    private final KeywordMetaRepository keywordRepository;
    private final KeywordMetaMapper keywordMapper = KeywordMetaMapper.INSTANCE;

    public List<KeywordMetaDetailDTO> getHotKeywords() {
        return keywordMapper.toDtoList(keywordRepository.findTop10ByOrderBySearchCountDesc());
    }

    @EventListener
    @Transactional
    public KeywordMetaDetailDTO saveKeyword(KeywordMetaCreateDTO keywordCreateDTO) {
        KeywordMeta keywordMeta = keywordRepository.findByKeyword(keywordCreateDTO.getKeyword())
                .orElse(KeywordMeta
                        .builder()
                        .keyword(keywordCreateDTO.getKeyword())
                        .build());
        keywordMeta.increaseSearchCount();

        return keywordMapper.toDto(keywordRepository.save(keywordMeta));
    }
}
