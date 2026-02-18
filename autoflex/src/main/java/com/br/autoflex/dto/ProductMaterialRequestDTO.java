package com.br.autoflex.dto;

import lombok.Data;

@Data
public class ProductMaterialRequestDTO {
    private Long rawMaterialId;
    private Integer quantityRequired;
}
