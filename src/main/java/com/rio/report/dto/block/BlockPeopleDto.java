package com.rio.report.dto.block;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlockPeopleDto {

    private String object;
    private String id;
    private String name;
    private String avatar_url;
    private String type;
    private Map<String, Object> person;
}
