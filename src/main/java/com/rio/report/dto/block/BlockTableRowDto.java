package com.rio.report.dto.block;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockTableRowDto {

    private List<List<BlockTableCellDto>> cells;
}
