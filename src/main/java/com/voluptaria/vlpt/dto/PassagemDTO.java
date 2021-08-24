package com.voluptaria.vlpt.dto;

import com.voluptaria.vlpt.model.Passagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassagemDTO {
    private Long id;
    private String origem;
    private String destino;
    private String dataIda;
    private String dataVolta;
    private Long idPacote;
    private Long idEmpresa;

    public static PassagemDTO createDTO(Passagem passagem) {
        ModelMapper modelMapper = new ModelMapper();
        PassagemDTO dto = modelMapper.map(passagem, PassagemDTO.class);
        assert dto.idEmpresa.equals(passagem.getEmpresa().getId());
        assert dto.idPacote.equals(passagem.getPacote().getId());
        return dto;
    }

}
