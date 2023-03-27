package com.rio.report.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PeopleConditionVo {

    private String property;
    private ContainsConditionVo people;

}
