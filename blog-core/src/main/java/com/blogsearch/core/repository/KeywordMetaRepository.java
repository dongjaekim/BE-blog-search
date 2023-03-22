package com.blogsearch.core.repository;

import com.blogsearch.core.model.KeywordMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordMetaRepository extends JpaRepository<KeywordMeta, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<KeywordMeta> findByKeyword(String Keyword);

    List<KeywordMeta> findTop10ByOrderBySearchCountDesc();
}
