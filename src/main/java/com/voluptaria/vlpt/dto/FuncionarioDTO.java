package com.voluptaria.vlpt.dto;

import com.voluptaria.vlpt.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {
    private Long id;
    private String cpf;
    private String nome;
    private String telefone;
    private String senha;
    private String email;
    private String tipoFuncionario;
    private String enderecoLogradouro;
    private String enderecoNumero;
    private String enderecoComplemento;
    private String enderecoUf;
    private String enderecoCidade;
    private String enderecoCep;

    public static FuncionarioDTO createDTO(Funcionario funcionario) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(funcionario, FuncionarioDTO.class);
    }

}
