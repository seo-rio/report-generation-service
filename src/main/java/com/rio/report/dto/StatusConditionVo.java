package com.rio.report.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusConditionVo {

    private String property;
    private EqualsConditionVo status;
}
