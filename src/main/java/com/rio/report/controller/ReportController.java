package com.rio.report.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rio.report.common.ApiResponse;
import com.rio.report.common.EnumResult;
import com.rio.report.common.MessageResponse;
import com.rio.report.common.ResultHeaders;
import com.rio.report.dto.GenerateReportDto;
import com.rio.report.dto.NotionResponseDto;
import com.rio.report.service.ReportService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;
    private final WebClient pushWebClient;

    @PostMapping("/report")
    public ResponseEntity<Object> generateReport(@RequestBody GenerateReportDto generateReportDto) {

        try {
            reportService.generateReport(generateReportDto);

            return ResponseEntity.status(HttpStatus.OK)
                    .headers(new ResultHeaders(EnumResult.SUCCESS))
                    .body(new ApiResponse<>(new MessageResponse("주간 보고서 생성에 성공하였습니다.")));
        } catch (Exception e) {
            log.error("", e);
            pushWebClient.delete().uri("/v1/blocks/" + generateReportDto.getReportPageId()).retrieve().bodyToMono(Void.class).block();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(new ResultHeaders(EnumResult.FAIL))
                    .body(new ApiResponse<>(new MessageResponse(e.getMessage())));
        }
    }
}
