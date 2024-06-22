package com.example.mini_community.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class CustomResponse {

    private final int statusCode;
    private final String message;
    private final Object data;

    public static CustomResponse response(final HttpStatusCode statusCode, final String message, final Object data) {
        return new CustomResponse(statusCode.value(), message, data);
    }

    public static CustomResponse response(final HttpStatusCode statusCode, final String message) {
        return new CustomResponse(statusCode.value(), message, new ArrayList<>());
    }
}
