package com.rio.report.dto.block;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlockTitleDto {

    private String type;
    private BlockTextDto text;

    @JsonProperty("plain_text")
    private String planText;
}
