package com.rio.report.dto.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockChildrenDto {

    @JsonProperty("table_row")
    private BlockTableRowDto tableRow;
}
