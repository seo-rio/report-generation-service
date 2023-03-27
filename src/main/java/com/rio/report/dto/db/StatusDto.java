package com.rio.report.dto.db;

import com.rio.report.dto.block.BlockStatusDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto extends CommonPropertyDto {

    private BlockStatusDto status;

}
