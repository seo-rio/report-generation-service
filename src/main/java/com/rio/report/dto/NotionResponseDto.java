package com.rio.report.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionResponseDto {

    private String Object;
    private List<HashMap<String, Object>> results;

}
