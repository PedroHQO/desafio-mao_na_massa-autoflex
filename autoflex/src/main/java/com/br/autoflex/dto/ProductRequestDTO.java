package com.br.autoflex.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequestDTO {
    private String name;
    private BigDecimal price;
    private List<ProductMaterialRequestDTO> materials;
}
