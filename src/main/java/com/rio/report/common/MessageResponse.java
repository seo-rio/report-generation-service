package com.rio.report.common;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class MessageResponse {

    private final Object message;
    private List<Map<String, Object>> errors;

    public static MessageResponse of(Object message, List<Map<String, Object>> errors) {
        return new MessageResponse(message, errors);
    }

    public static MessageResponse of(Object message) {
        return new MessageResponse(message);
    }

    public MessageResponse(Object message, List<Map<String, Object>> errors) {
        this.message = message;
        this.errors = errors;
    }

    public MessageResponse(Object message) {
        this.message = message;
    }
}
