package com.voluptaria.vlpt.controller;

import com.voluptaria.vlpt.dto.FuncionarioDTO;
import com.voluptaria.vlpt.dto.PacoteDTO;
import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Funcionario;
import com.voluptaria.vlpt.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService service;

    @GetMapping()
    public ResponseEntity getAll() {
        List<Funcionario> funcionarios = service.getFuncionarios();
        return ResponseEntity.ok(funcionarios.stream().map(FuncionarioDTO::createDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Funcionario> funcionario = service.getFuncionarioById(id);
        if(!funcionario.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Funcionário não encontrado");
        }
       return ResponseEntity.ok(FuncionarioDTO.createDTO(funcionario.get()));
    }

    @GetMapping("/{id}/pacotes")
    public ResponseEntity getPacotes(@PathVariable Long id){
        Optional<Funcionario> funcionario = service.getFuncionarioById(id);
        if(!funcionario.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Funcionário não encontrado");
        }
        return ResponseEntity.ok(funcionario.get().getPacotes()
                .stream().map(PacoteDTO::createDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody FuncionarioDTO funcionarioDTO){
        try {
            Funcionario funcionario = convertToModel(funcionarioDTO);
            Funcionario funcionarioSalvo = service.save(funcionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalvo);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable Long id,@RequestBody FuncionarioDTO funcionarioDTO){
        if(!service.getFuncionarioById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionario não Encontrado");
        }
        try {
            Funcionario funcionario = convertToModel(funcionarioDTO);
            funcionario.setId(id);
            service.update(funcionario);
            return ResponseEntity.ok(funcionario);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        Optional<Funcionario> funcionario = service.getFuncionarioById(id);
        if(!funcionario.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Funcionario não encontrado");
        }
        service.delete(funcionario.get());
        return ResponseEntity.noContent().build();
    }

    private Funcionario convertToModel(FuncionarioDTO funcionarioDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(funcionarioDTO, Funcionario.class);
    }
}
