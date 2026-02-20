package com.br.autoflex.service;

import com.br.autoflex.domain.Product;
import com.br.autoflex.domain.ProductMaterial;
import com.br.autoflex.domain.RawMaterial;
import com.br.autoflex.dto.ProductMaterialRequestDTO;
import com.br.autoflex.dto.ProductRequestDTO;
import com.br.autoflex.dto.ProductionSuggestionDTO;
import com.br.autoflex.dto.ProductionSuggestionItemDTO;
import com.br.autoflex.repository.ProductRepository;
import com.br.autoflex.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Transactional
    public Product createProduct(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        if (dto.getMaterials() != null) {
            for (ProductMaterialRequestDTO materialDto : dto.getMaterials()) {
                RawMaterial rawMaterial = rawMaterialRepository.findById(materialDto.getRawMaterialId())
                        .orElseThrow(() -> new RuntimeException(
                                "RawMaterial not found with id: " + materialDto.getRawMaterialId()));

                ProductMaterial productMaterial = new ProductMaterial();
                productMaterial.setRawMaterial(rawMaterial);
                productMaterial.setQuantityRequired(materialDto.getQuantityRequired());
                productMaterial.setProduct(product);

                product.getProductMaterials().add(productMaterial);
            }
        }

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public ProductionSuggestionDTO calculateProductionSuggestion() {
        List<Product> products = productRepository.findAllByOrderByPriceDesc();

        List<RawMaterial> allMaterials = rawMaterialRepository.findAll();
        Map<Long, Integer> stockMap = allMaterials.stream()
                .collect(Collectors.toMap(RawMaterial::getId, RawMaterial::getStockQuantity));

        List<ProductionSuggestionItemDTO> suggestionItems = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            int maxProducibleQuantity = Integer.MAX_VALUE;

            if (product.getProductMaterials().isEmpty()) {
                maxProducibleQuantity = 0;
                continue;
            }

            for (ProductMaterial pm : product.getProductMaterials()) {
                Long rawMaterialId = pm.getRawMaterial().getId();
                Integer required = pm.getQuantityRequired();
                if (required == null || required <= 0) {
                    continue;
                }
                Integer available = stockMap.getOrDefault(rawMaterialId, 0);

                int possibleWithThisMaterial = available / required;
                if (possibleWithThisMaterial < maxProducibleQuantity) {
                    maxProducibleQuantity = possibleWithThisMaterial;
                }
            }

            if (maxProducibleQuantity > 0) {
                ProductionSuggestionItemDTO itemDTO = new ProductionSuggestionItemDTO();
                itemDTO.setProductName(product.getName());
                itemDTO.setQuantityToProduce(maxProducibleQuantity);
                suggestionItems.add(itemDTO);

                BigDecimal productTotal = product.getPrice().multiply(BigDecimal.valueOf(maxProducibleQuantity));
                totalValue = totalValue.add(productTotal);

                for (ProductMaterial pm : product.getProductMaterials()) {
                    Long rawMaterialId = pm.getRawMaterial().getId();
                    Integer required = pm.getQuantityRequired();
                    Integer used = required * maxProducibleQuantity;

                    Integer currentStock = stockMap.get(rawMaterialId);
                    stockMap.put(rawMaterialId, currentStock - used);
                }
            }
        }

        ProductionSuggestionDTO suggestionDTO = new ProductionSuggestionDTO();
        suggestionDTO.setTotalValue(totalValue);
        suggestionDTO.setProducts(suggestionItems);

        return suggestionDTO;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        product.getProductMaterials().clear();

        if (dto.getMaterials() != null) {
            for (ProductMaterialRequestDTO materialDto : dto.getMaterials()) {
                RawMaterial rawMaterial = rawMaterialRepository.findById(materialDto.getRawMaterialId())
                        .orElseThrow(() -> new RuntimeException("Material not found"));

                ProductMaterial pm = new ProductMaterial();
                pm.setRawMaterial(rawMaterial);
                pm.setQuantityRequired(materialDto.getQuantityRequired());
                pm.setProduct(product);

                product.getProductMaterials().add(pm);
            }
        }

        return productRepository.save(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
