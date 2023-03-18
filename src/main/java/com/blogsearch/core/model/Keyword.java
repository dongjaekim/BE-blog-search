package com.blogsearch.core.model;

import com.blogsearch.core.model.base.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Keyword extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String keyword;

    @NotNull
    @Builder.Default
    private Long searchCount = 1L;

    public void increaseSearchCount() {
        this.searchCount++;
    }

}
