package com.br.autoflex.service;

import com.br.autoflex.domain.RawMaterial;
import com.br.autoflex.dto.RawMaterialRequestDTO;
import com.br.autoflex.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterial create(RawMaterialRequestDTO dto) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName(dto.getName());
        rawMaterial.setStockQuantity(dto.getStockQuantity());
        return rawMaterialRepository.save(rawMaterial);
    }

    public List<RawMaterial> findAll() {
        return rawMaterialRepository.findAll();
    }

    @Transactional
    public RawMaterial update(Long id, RawMaterialRequestDTO dto) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        rawMaterial.setName(dto.getName());
        rawMaterial.setStockQuantity(dto.getStockQuantity());

        return rawMaterialRepository.save(rawMaterial);
    }

    public void delete(Long id) {
        if (!rawMaterialRepository.existsById(id)) {
            throw new RuntimeException("Material not found");
        }

        rawMaterialRepository.deleteById(id);
    }

}
