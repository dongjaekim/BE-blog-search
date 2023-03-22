package com.blogsearch.core.utils.mapper;

import com.blogsearch.core.dto.KeywordMetaDetailDTO;
import com.blogsearch.core.model.KeywordMeta;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeywordMetaMapper {
    KeywordMetaMapper INSTANCE = Mappers.getMapper(KeywordMetaMapper.class);

    KeywordMetaDetailDTO toDto(KeywordMeta keyword);

    List<KeywordMetaDetailDTO> toDtoList(List<KeywordMeta> keywordList);
}
