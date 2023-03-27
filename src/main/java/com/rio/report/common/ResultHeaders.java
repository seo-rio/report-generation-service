package com.rio.report.common;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Slf4j
public class ResultHeaders extends HttpHeaders{

    public ResultHeaders(EnumResult result, String mediaType) {
        super(setHeaders(result, mediaType));
    }
    public ResultHeaders(EnumResult result) {
        super(setHeaders(result, null));
    }
    public ResultHeaders(EnumResult result, Map<String, Object> tokenMap) {
        super(setHeaders(result, null));
    }

    private static HttpHeaders setHeaders(EnumResult result, String mediaType) {
        final String RESULT_CODE_NAME = "Server-Result-Code";
        final String RESULT_MESSAGE_NAME = "Server-Result-Message";

        Map<String, Object> header = new HashMap<>();
        header.put(RESULT_CODE_NAME, result.getCd());
        header.put(RESULT_MESSAGE_NAME, result.getMsg());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(mediaType != null ? mediaType : MediaType.APPLICATION_JSON_VALUE));

        for (String key : header.keySet()) {
            headers.set(key, String.valueOf(header.get(key)));
        }
        return headers;
    }
}
