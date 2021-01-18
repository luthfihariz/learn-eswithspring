package com.luthfihariz.esbasic;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class Product {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String category;
}
