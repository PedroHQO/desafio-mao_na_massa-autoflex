package com.br.autoflex.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "raw_materials")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer stockQuantity;
}
