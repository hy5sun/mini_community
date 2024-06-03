package com.example.mini_community.common.type;

import com.example.mini_community.common.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.example.mini_community.common.exception.ErrorCode.WRONG_SEARCH_TYPE;

@Getter
@AllArgsConstructor
public enum SearchType {
    TITLE("TITLE"),
    CONTENT("CONTENT"),
    WRITER("WRITER");

    private final String value;

    @JsonCreator
    public static SearchType fromSearchType(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new BusinessException(WRONG_SEARCH_TYPE));
    }
}
