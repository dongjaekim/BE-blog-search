package com.blogsearch.core.service;

import com.blogsearch.core.dto.KeywordCreateDTO;
import com.blogsearch.core.dto.KeywordDetailDTO;
import com.blogsearch.core.utils.mapper.KeywordMapper;
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
    private final KeywordMapper keywordMapper = KeywordMapper.INSTANCE;

    public List<KeywordDetailDTO> getHotKeywords() {
        return keywordMapper.toDtoList(keywordRepository.findTop10ByOrderBySearchCountDesc());
    }

    @EventListener
    @Transactional
    public KeywordDetailDTO saveKeyword(KeywordCreateDTO keywordCreateDTO) {
        KeywordMeta keywordMeta = keywordRepository.findByKeyword(keywordCreateDTO.getKeyword())
                .orElse(KeywordMeta
                        .builder()
                        .keyword(keywordCreateDTO.getKeyword())
                        .build());
        keywordMeta.increaseSearchCount();

        return keywordMapper.toDto(keywordRepository.save(keywordMeta));
    }
}
