package com.br.autoflex.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductionSuggestionDTO {
    private BigDecimal totalValue;
    private List<ProductionSuggestionItemDTO> products;
}
