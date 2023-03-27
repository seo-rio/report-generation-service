package com.rio.report.dto.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlockDateDto {

    private String start;
    private String end;
    private String timezone;
}
