package com.rio.report.dto.db;

import com.rio.report.dto.block.BlockChildrenDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NotionDataDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String object;
        private List<WorkResultDto> results;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private List<BlockChildrenDto> children;
    }
}
