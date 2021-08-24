package com.voluptaria.vlpt.service;

import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Passagem;
import com.voluptaria.vlpt.repository.PassagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassagemService {

    private final PassagemRepository repository;

    public List<Passagem> getPassagens(){
        return repository.findAll();
    }
    public Optional<Passagem> getPassagemById(Long id){
        return repository.findById(id);
    }

    @Transactional
    public Passagem save(Passagem passagem) {
        validar(passagem);
        return repository.save(passagem);
    }

    @Transactional
    public Passagem update(Passagem passagem) {
        Objects.requireNonNull(passagem.getId());
        validar(passagem);
        return repository.save(passagem);
    }

    @Transactional
    public void delete(Passagem passagem) {
        repository.delete(passagem);
    }

    public void validar(Passagem passagem) {
        if(passagem.getOrigem() == null || passagem.getOrigem().trim().equals("")){
            throw new RegraNegocioException("Origem inválida");
        }
        if(passagem.getDestino() == null || passagem.getDestino().trim().equals("")){
            throw new RegraNegocioException("Destino inválido");
        }
        if(passagem.getDataIda() == null || passagem.getDataIda().trim().equals("")){
            throw new RegraNegocioException("Nome inválido");
        }
        if(LocalDate.parse(passagem.getDataVolta()).isBefore(LocalDate.parse(passagem.getDataIda()))){
            throw new RegraNegocioException("Data de volta deve ser posterior a data de ida");
        }

    }
}
