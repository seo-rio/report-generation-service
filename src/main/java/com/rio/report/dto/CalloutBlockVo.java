package com.rio.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalloutBlockVo {

    @JsonProperty("rich_text")
    private List<RichTextVo> richText;
    private IconVo icon;
    private String color;
}
