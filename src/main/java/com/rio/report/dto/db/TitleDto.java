package com.rio.report.dto.db;

import com.rio.report.dto.block.BlockTitleDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TitleDto extends CommonPropertyDto {

    private List<BlockTitleDto> title;

}
