package com.voluptaria.vlpt.service;

import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Cliente;
import com.voluptaria.vlpt.repository.ClienteRepository;
import com.voluptaria.vlpt.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final EnderecoRepository enderecoRepository;

    public List<Cliente> getClientes() {
        return repository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        validar(cliente);
        cliente.setEndereco(enderecoRepository.save(cliente.getEndereco()));
        return repository.save(cliente);
    }

    @Transactional
    public Cliente update(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        Cliente clienteSalvo = repository.getById(cliente.getId());
        cliente.getEndereco().setId(clienteSalvo.getEndereco().getId());
        validar(cliente);
        return save(cliente);
    }

    @Transactional
    public void delete(Cliente cliente) {
        repository.delete(cliente);
    }

    private void validar(Cliente cliente) {
        if(cliente.getCpf() == null || cliente.getCpf().trim().equals("")){
            throw new RegraNegocioException("CPF inválido");
        }
        if(cliente.getNome() == null || cliente.getNome().trim().equals("")){
            throw new RegraNegocioException("Nome inválido");
        }
        if(cliente.getEmail() == null || cliente.getEmail().trim().equals("")){
            throw new RegraNegocioException("Email inválido");
        }
    }



}
