package com.rio.report.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DateConditionVo {

    private String property;
    private Map<String, Object> date;
}
