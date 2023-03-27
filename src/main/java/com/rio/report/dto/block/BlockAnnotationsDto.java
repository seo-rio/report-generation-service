package com.rio.report.dto.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockAnnotationsDto {

    private boolean bold;
    private boolean italic;
    private boolean strikethrough;
    private boolean underline;
    private boolean code;
    private String color;
}
