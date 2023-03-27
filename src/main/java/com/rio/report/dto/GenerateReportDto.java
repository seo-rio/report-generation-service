package com.rio.report.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateReportDto {

    private String reportPageId;
    private List<String> teamEmailAddr;
    private List<String> taskLinks;
}
