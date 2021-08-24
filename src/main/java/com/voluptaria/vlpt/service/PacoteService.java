package com.voluptaria.vlpt.service;

import com.voluptaria.vlpt.model.Pacote;
import com.voluptaria.vlpt.repository.PacoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacoteService {

    private final PacoteRepository repository;

    public List<Pacote> getPacotes() {
        return repository.findAll();
    }

    public Optional<Pacote> getPacoteById(Long id){
       return repository.findById(id);
    }

    @Transactional
    public Pacote save(Pacote pacote) {
        validar(pacote);
        return repository.save(pacote);
    }

    @Transactional
    public Pacote update(Pacote pacote) {
        Objects.requireNonNull(pacote.getId());
        return save(pacote);
    }

    @Transactional
    public void delete(Pacote pacote) {

        repository.delete(pacote);
    }

    private void validar(Pacote pacote) {
    }
}
