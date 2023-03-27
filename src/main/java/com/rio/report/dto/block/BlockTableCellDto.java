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
public class BlockTableCellDto {

    private String type;
    private BlockTextDto text;
    private BlockAnnotationsDto annotations;
}
