package com.luthfihariz.esbasic.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortRequestDto {
    private String field;
    private SortOrder order;
}
