package com.blogsearch.core.utils.mapper;

import com.blogsearch.core.dto.BlogSearchDetailDTO;
import com.blogsearch.core.dto.external.KakaoBlogSearchDetailDTO;
import com.blogsearch.core.dto.external.NaverBlogSearchDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SearchDetailMapper {
    SearchDetailMapper INSTANCE = Mappers.getMapper(SearchDetailMapper.class);

    default BlogSearchDetailDTO toBlogSearchDetailDTO(KakaoBlogSearchDetailDTO kakaoBlogSearchDetailDTO) {
        int total_count = kakaoBlogSearchDetailDTO.getMeta().getTotal_count();
        List<BlogSearchDetailDTO.Content> contents = new ArrayList<>(kakaoBlogSearchDetailDTO.getDocuments().size());

        for (KakaoBlogSearchDetailDTO.Document document : kakaoBlogSearchDetailDTO.getDocuments())
            contents.add(toContent(document));

        return BlogSearchDetailDTO.builder()
                .total_count(total_count)
                .contents(contents)
                .build();
    }

    default BlogSearchDetailDTO toBlogSearchDetailDTO(NaverBlogSearchDetailDTO naverBlogSearchDetailDTO) {
        int total_count = naverBlogSearchDetailDTO.getTotal();
        List<BlogSearchDetailDTO.Content> contents = new ArrayList<>(naverBlogSearchDetailDTO.getDisplay());

        for (NaverBlogSearchDetailDTO.Item item : naverBlogSearchDetailDTO.getItems())
            contents.add(toContent(item));


        return BlogSearchDetailDTO.builder()
                .total_count(total_count)
                .contents(contents)
                .build();
    }

    BlogSearchDetailDTO.Content toContent(KakaoBlogSearchDetailDTO.Document document);

    @Mapping(source = "item.bloggername", target = "blogname")
    @Mapping(source = "item.description", target = "contents")
    @Mapping(source = "item.link", target = "url")
    @Mapping(expression = "java(java.time.LocalDateTime.of(Integer.parseInt(item.getPostdate().substring(0, 4))" +
            ", Integer.parseInt(item.getPostdate().substring(4, 6))" +
            ", Integer.parseInt(item.getPostdate().substring(6))" +
            ", 0, 0))", target = "datetime")
    BlogSearchDetailDTO.Content toContent(NaverBlogSearchDetailDTO.Item item);
}
