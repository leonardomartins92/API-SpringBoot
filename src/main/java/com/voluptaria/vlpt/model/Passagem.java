package com.voluptaria.vlpt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origem;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private String dataIda;

    private String dataVolta;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pacote_id")
    private Pacote pacote;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
