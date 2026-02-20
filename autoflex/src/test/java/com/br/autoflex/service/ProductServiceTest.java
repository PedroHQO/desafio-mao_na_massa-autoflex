package com.br.autoflex.service;

import com.br.autoflex.domain.Product;
import com.br.autoflex.domain.ProductMaterial;
import com.br.autoflex.domain.RawMaterial;
import com.br.autoflex.dto.ProductionSuggestionDTO;
import com.br.autoflex.dto.ProductionSuggestionItemDTO;
import com.br.autoflex.repository.ProductRepository;
import com.br.autoflex.repository.RawMaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Deve calcular a sugestão de produção corretamente e deduzir os insumos em memória")
    void calculateProductionSuggestion_Success() {

        RawMaterial flour = createRawMaterial(1L, "Flour", 2000);
        RawMaterial eggs = createRawMaterial(2L, "Eggs", 30);
        RawMaterial chocolate = createRawMaterial(3L, "Chocolate", 10);

        List<RawMaterial> mockStock = Arrays.asList(flour, eggs, chocolate);

        Product chocolateCake = new Product();
        chocolateCake.setId(1L);
        chocolateCake.setName("Chocolate Cake");
        chocolateCake.setPrice(new BigDecimal("27.0"));
        chocolateCake.getProductMaterials().add(createProductMaterial(chocolateCake, flour, 200));
        chocolateCake.getProductMaterials().add(createProductMaterial(chocolateCake, eggs, 3));
        chocolateCake.getProductMaterials().add(createProductMaterial(chocolateCake, chocolate, 5));

        Product normalCake = new Product();
        normalCake.setId(2L);
        normalCake.setName("Cake");
        normalCake.setPrice(new BigDecimal("20.0"));
        normalCake.getProductMaterials().add(createProductMaterial(normalCake, flour, 200));
        normalCake.getProductMaterials().add(createProductMaterial(normalCake, eggs, 3));

        List<Product> mockProducts = Arrays.asList(chocolateCake, normalCake);

        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(mockProducts);
        when(rawMaterialRepository.findAll()).thenReturn(mockStock);

        ProductionSuggestionDTO result = productService.calculateProductionSuggestion();

        assertNotNull(result);

        assertEquals(2, result.getProducts().size());

        ProductionSuggestionItemDTO chocItem = result.getProducts().get(0);
        assertEquals("Chocolate Cake", chocItem.getProductName());
        assertEquals(2, chocItem.getQuantityToProduce());

        ProductionSuggestionItemDTO normalItem = result.getProducts().get(1);
        assertEquals("Cake", normalItem.getProductName());
        assertEquals(8, normalItem.getQuantityToProduce());

        assertEquals(new BigDecimal("214.0"), result.getTotalValue());
    }

    @Test
    @DisplayName("Deve retornar sugestão vazia quando o estoque das matérias-primas for zero")
    void calculateProductionSuggestion_EmptyStock() {

        RawMaterial flour = createRawMaterial(1L, "Flour", 0);
        RawMaterial eggs = createRawMaterial(2L, "Eggs", 0);

        List<RawMaterial> mockStock = Arrays.asList(flour, eggs);

        Product cake = new Product();
        cake.setId(1L);
        cake.setName("Cake");
        cake.setPrice(new BigDecimal("20.0"));
        cake.getProductMaterials().add(createProductMaterial(cake, flour, 200));
        cake.getProductMaterials().add(createProductMaterial(cake, eggs, 3));

        List<Product> mockProducts = Arrays.asList(cake);

        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(mockProducts);
        when(rawMaterialRepository.findAll()).thenReturn(mockStock);

        ProductionSuggestionDTO result = productService.calculateProductionSuggestion();

        assertNotNull(result);

        assertTrue(result.getProducts().isEmpty(),
                "A lista de sugestões deveria estar vazia devido à falta de estoque");

        assertEquals(BigDecimal.ZERO, result.getTotalValue(), "O valor total deveria ser zero");
    }

    private RawMaterial createRawMaterial(Long id, String name, int stock) {
        RawMaterial rm = new RawMaterial();
        rm.setId(id);
        rm.setName(name);
        rm.setStockQuantity(stock);
        return rm;
    }

    private ProductMaterial createProductMaterial(Product product, RawMaterial rawMaterial, int quantity) {
        ProductMaterial pm = new ProductMaterial();
        pm.setProduct(product);
        pm.setRawMaterial(rawMaterial);
        pm.setQuantityRequired(quantity);
        return pm;
    }
}