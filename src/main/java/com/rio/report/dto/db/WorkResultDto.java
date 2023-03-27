package com.rio.report.dto.db;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkResultDto {

    private String object;
    private WorkPropertyDto properties;
}
