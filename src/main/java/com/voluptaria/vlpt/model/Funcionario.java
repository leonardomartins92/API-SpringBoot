package com.voluptaria.vlpt.model;

import com.voluptaria.vlpt.enums.TipoFuncionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column
    private String telefone;

    @Column(nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoFuncionario tipoFuncionario;

    @OneToMany(mappedBy = "funcionario")
    private List<Pacote> pacotes;
}
