package com.luthfihariz.esbasic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchQueryDto {
    private String query;
    private FilterRequestDto filter;
    private Integer page;
    private Integer size;
}
