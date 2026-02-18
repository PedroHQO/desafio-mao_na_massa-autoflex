package com.br.autoflex.controller;

import com.br.autoflex.domain.Product;
import com.br.autoflex.dto.ProductRequestDTO;
import com.br.autoflex.dto.ProductionSuggestionDTO;
import com.br.autoflex.repository.ProductRepository;
import com.br.autoflex.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequestDTO dto) {
        Product created = productService.createProduct(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {

        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/suggestion")
    public ResponseEntity<ProductionSuggestionDTO> getSuggestion() {
        return ResponseEntity.ok(productService.calculateProductionSuggestion());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
