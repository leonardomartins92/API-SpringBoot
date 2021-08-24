package com.voluptaria.vlpt.service;

import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Funcionario;
import com.voluptaria.vlpt.model.Pacote;
import com.voluptaria.vlpt.repository.EnderecoRepository;
import com.voluptaria.vlpt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final EnderecoRepository enderecoRepository;
    private final PacoteService pacoteService;

    public List<Funcionario> getFuncionarios(){
        return repository.findAll();
    }

    public Optional<Funcionario> getFuncionarioById(Long id){
        return repository.findById(id);
    }

    @Transactional
    public Funcionario save(Funcionario funcionario) {
        validar(funcionario);
        funcionario.setEndereco(enderecoRepository.save(funcionario.getEndereco()));
        return repository.save(funcionario);
    }

    @Transactional
    public Funcionario update(Funcionario funcionario) {
        Objects.requireNonNull(funcionario.getId());
        Funcionario funcionarioSalvo = repository.getById(funcionario.getId());
        funcionario.getEndereco().setId(funcionarioSalvo.getEndereco().getId());
        return save(funcionario);
    }

    @Transactional
    public void delete(Funcionario funcionario) {
        for(Pacote pacote: funcionario.getPacotes()){
            pacote.setFuncionario(null);
            pacoteService.save(pacote);
        }
         repository.delete(funcionario);
    }

    private void validar(Funcionario funcionario) {
        if(funcionario.getCpf() == null || funcionario.getCpf().trim().equals("")){
            throw new RegraNegocioException("CPF inválido");
        }
        if(funcionario.getNome() == null || funcionario.getNome().trim().equals("")){
            throw new RegraNegocioException("Nome inválido");
        }
        if(funcionario.getEmail() == null || funcionario.getEmail().trim().equals("")){
            throw new RegraNegocioException("Email inválido");
        }
        if(funcionario.getSenha() == null || funcionario.getSenha().trim().equals("")){
            throw new RegraNegocioException("Senha inválida");
        }
    }
}
