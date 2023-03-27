package com.rio.report.dto.db;

import com.rio.report.dto.block.BlockChildrenDto;
import com.rio.report.dto.block.BlockPeopleDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NotionPersonDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String object;
        private List<BlockPeopleDto> results;
    }
}
