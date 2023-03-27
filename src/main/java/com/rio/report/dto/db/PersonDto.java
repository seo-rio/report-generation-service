package com.rio.report.dto.db;

import com.rio.report.dto.block.BlockPeopleDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto extends CommonPropertyDto{

    private List<BlockPeopleDto> people;

}
