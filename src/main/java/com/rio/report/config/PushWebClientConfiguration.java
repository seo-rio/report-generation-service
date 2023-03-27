package com.rio.report.config;

import com.rio.report.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class PushWebClientConfiguration {


    @Bean
    public WebClient pushWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.notion.com")
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
                httpHeaders.set("Notion-Version", "2022-02-22");
            })
            .filter(ExchangeFilterFunction.ofRequestProcessor(
                clientRequest -> {
                    log.debug("========== REQUEST: {} {}", clientRequest.method(), clientRequest.url());
                    clientRequest.headers()
                        .forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                    log.debug(LogUtil.printPoint("REQUEST"));
                    return Mono.just(clientRequest);
                }
            ))
            .filter(ExchangeFilterFunction.ofResponseProcessor(
                clientResponse -> {
                    log.debug(LogUtil.printPoint("RESPONSE"));
                    clientResponse.headers()
                        .asHttpHeaders()
                        .forEach((name, values) ->
                            values.forEach(value -> log.debug("{} : {}", name, value)));
                    log.debug(LogUtil.printPoint("RESPONSE"));
                    return Mono.just(clientResponse);
                }
            ))
            .build();
    }
}
