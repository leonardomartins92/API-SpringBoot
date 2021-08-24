package com.voluptaria.vlpt.dto;

import com.voluptaria.vlpt.model.Destino;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinoDTO {
    private Long id;
    private  String dataInicial;
    private String dataFinal;
    private Long idPacote;
    private Long idEmpresa;
    private String nomeEmpresa;

    public static DestinoDTO createDTO(Destino destino) {
        ModelMapper modelMapper = new ModelMapper();
        DestinoDTO dto = modelMapper.map(destino, DestinoDTO.class);
        assert dto.getIdPacote().equals(destino.getPacote().getId());
        assert dto.getIdEmpresa().equals(destino.getEmpresa().getId());
        assert dto.getNomeEmpresa().equals(destino.getEmpresa().getNome());
        return dto;
    }

}
