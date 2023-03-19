package com.blogsearch.core.repository;

import com.blogsearch.core.model.KeywordMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordMetaRepository extends JpaRepository<KeywordMeta, Long> {

    Optional<KeywordMeta> findByKeyword(String Keyword);

    List<KeywordMeta> findTop10ByOrderBySearchCountDesc();
}
