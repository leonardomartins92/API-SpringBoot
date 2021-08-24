package com.voluptaria.vlpt.dto;

import com.voluptaria.vlpt.model.Empresa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private Long id;
    private String cnpj;
    private String nome;
    private String telefone;
    private String email;
    private String tipoEmpresa;
    private String enderecoLogradouro;
    private String enderecoNumero;
    private String enderecoComplemento;
    private String enderecoUf;
    private String enderecoCidade;
    private String enderecoCep;

    public static EmpresaDTO createDTO(Empresa empresa) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(empresa, EmpresaDTO.class);
    }

}
