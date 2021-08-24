package com.voluptaria.vlpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logradouro;

    @Column(nullable = false)
    private String numero;

    private String complemento;

    private String uf;

    private String cidade;

    @Column(nullable = false)
    private String cep;
}
