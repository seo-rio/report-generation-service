package com.rio.report.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private T result;

    public ApiResponse(T result) {
        this.result = result;
    }
}
