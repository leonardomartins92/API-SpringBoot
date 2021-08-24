package com.voluptaria.vlpt.service;

import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Empresa;
import com.voluptaria.vlpt.repository.EmpresaRepository;
import com.voluptaria.vlpt.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository repository;

    private final EnderecoRepository enderecoRepository;

    public List<Empresa> getEmpresas(){
        return repository.findAll();
    }
    public Optional<Empresa> getEmpresaById(Long id){
        return repository.findById(id);
    }

    @Transactional
    public Empresa save(Empresa empresa) {
        validar(empresa);
        empresa.setEndereco(enderecoRepository.save(empresa.getEndereco()));
        return repository.save(empresa);
    }

    @Transactional
    public Empresa update(Empresa empresa) {
        Objects.requireNonNull(empresa.getId());
        Empresa empresaSalvo = repository.getById(empresa.getId());
        empresa.getEndereco().setId(empresaSalvo.getEndereco().getId());
        return save(empresa);
    }

    @Transactional
    public void delete(Empresa empresa) {
        repository.delete(empresa);
    }

    private void validar(Empresa empresa) {
        if(empresa.getCnpj() == null || empresa.getCnpj().trim().equals("")){
            throw new RegraNegocioException("CNPJ inválido");
        }
        if(empresa.getNome() == null || empresa.getNome().trim().equals("")){
            throw new RegraNegocioException("Nome inválido");
        }
        if(empresa.getEmail() == null || empresa.getEmail().trim().equals("")){
            throw new RegraNegocioException("Email inválido");
        }
    }
}
