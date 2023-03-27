package com.rio.report.dto.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkPropertyDto {

    @JsonProperty("계획 기간")
    private PlanPeriodDto planPeriod;

    @JsonProperty("상태")
    private StatusDto workStatus;

    @JsonProperty("담당자")
    private PersonDto workPerson;

    @JsonProperty("제목")
    private TitleDto title;
}
