package com.rio.report.dto.db;

import com.rio.report.dto.block.BlockDateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanPeriodDto extends CommonPropertyDto {

    private BlockDateDto date;


}
