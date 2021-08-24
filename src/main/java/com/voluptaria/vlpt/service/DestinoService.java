package com.voluptaria.vlpt.service;

import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Destino;
import com.voluptaria.vlpt.repository.DestinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinoService {

    private final DestinoRepository repository;

    public List<Destino> getDestinos(){
        return repository.findAll();
    }

    public Optional<Destino> getDestinoById(Long id){
        return repository.findById(id);
    }

    @Transactional
    public Destino save(Destino destino) {
        validar(destino);
        return repository.save(destino);
    }

    @Transactional
    public Destino update(Destino destino) {
        Objects.requireNonNull(destino.getId());
        return save(destino);
    }

    @Transactional
    public void delete(Destino destino) {
        repository.delete(destino);
    }

    private void validar(Destino destino) {
        if(destino.getDataInicial() == null || destino.getDataInicial().trim().equals("")){
            throw new RegraNegocioException("Data Inicial inválida");
        }
        if(LocalDate.parse(destino.getDataFinal()).isBefore(LocalDate.parse(destino.getDataInicial()))){
            throw new RegraNegocioException("Data final deve ser posterior a data inicial");
        }
    }
}
