package com.blogsearch.utils.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortParam {

    KAKAO("accuracy", "recency"),
    NAVER("sim", "date");

    private final String sortAccuracy;
    private final String sortRecency;

    public String mapSortValue(String value) {
        if (this == KAKAO) {
            if (KAKAO.sortAccuracy.equals(value))
                return NAVER.sortAccuracy;
            else if (KAKAO.sortRecency.equals(value))
                return NAVER.sortRecency;
        } else if (this == NAVER) {
            if (NAVER.sortAccuracy.equals(value))
                return KAKAO.sortAccuracy;
            else if (NAVER.sortRecency.equals(value))
                return KAKAO.sortRecency;
        }
        return null;
    }
}
