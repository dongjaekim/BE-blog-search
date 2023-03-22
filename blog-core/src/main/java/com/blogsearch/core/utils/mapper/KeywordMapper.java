package com.blogsearch.core.utils.mapper;

import com.blogsearch.core.dto.KeywordDetailDTO;
import com.blogsearch.core.model.KeywordMeta;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeywordMapper {
    KeywordMapper INSTANCE = Mappers.getMapper(KeywordMapper.class);

    KeywordDetailDTO toDto(KeywordMeta keyword);

    List<KeywordDetailDTO> toDtoList(List<KeywordMeta> keywordList);
}
