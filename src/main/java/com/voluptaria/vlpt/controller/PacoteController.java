package com.voluptaria.vlpt.controller;

import com.voluptaria.vlpt.dto.DestinoDTO;
import com.voluptaria.vlpt.dto.PacoteDTO;
import com.voluptaria.vlpt.dto.PassagemDTO;
import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Cliente;
import com.voluptaria.vlpt.model.Funcionario;
import com.voluptaria.vlpt.model.Pacote;
import com.voluptaria.vlpt.service.ClienteService;
import com.voluptaria.vlpt.service.FuncionarioService;
import com.voluptaria.vlpt.service.PacoteService;
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
@RequestMapping("/api/v1/pacotes")
@RequiredArgsConstructor
public class PacoteController {

    private final PacoteService service;
    private final FuncionarioService funcionarioService;
    private final ClienteService clienteService;

    @GetMapping()
    public ResponseEntity getAll() {
        List<Pacote> pacotes = service.getPacotes();
        return ResponseEntity.ok(pacotes.stream().map(PacoteDTO::createDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Pacote> pacote = service.getPacoteById(id);
        if(pacote.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pacote não encontrado");
        }
       return ResponseEntity.ok(PacoteDTO.createDTO(pacote.get()));
    }

    @GetMapping("/{id}/pacotes")
    public ResponseEntity getDestinos(@PathVariable Long id){
        Optional<Pacote> pacote = service.getPacoteById(id);
        if(pacote.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pacote não encontrado");
        }
        return ResponseEntity.ok(pacote.get().getDestinos()
                .stream().map(DestinoDTO::createDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}/passagens")
    public ResponseEntity getPassagens(@PathVariable Long id){
        Optional<Pacote> pacote = service.getPacoteById(id);
        if(pacote.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pacote não encontrado");
        }
        return ResponseEntity.ok(pacote.get().getPassagens()
                .stream().map(PassagemDTO::createDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody PacoteDTO pacoteDTO){
        try {
            Pacote pacote = convertToModel(pacoteDTO);
            Pacote pacoteSalvo = service.save(pacote);
            return ResponseEntity.status(HttpStatus.CREATED).body(pacoteSalvo);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable Long id,@RequestBody PacoteDTO pacoteDTO){
        if(service.getPacoteById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pacote não Encontrado");
        }
        try {
            Pacote pacote = convertToModel(pacoteDTO);
            pacote.setId(id);
            service.update(pacote);
            return ResponseEntity.ok(pacote);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        Optional<Pacote> pacote = service.getPacoteById(id);
        if(pacote.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pacote não encontrado");
        }
        service.delete(pacote.get());
        return ResponseEntity.noContent().build();
    }

    private Pacote convertToModel(PacoteDTO pacoteDTO){
        ModelMapper modelMapper = new ModelMapper();
        Pacote pacote = modelMapper.map(pacoteDTO, Pacote.class);

        if(pacoteDTO.getIdFuncionario() != null){
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(pacoteDTO.getIdFuncionario());
            if(funcionario.isEmpty()){
                pacote.setFuncionario(null);
            }else {
                pacote.setFuncionario(funcionario.get());
            }
        }
        if(pacoteDTO.getIdCliente() != null){
            Optional<Cliente> cliente = clienteService.getClienteById(pacoteDTO.getIdCliente());
            if(cliente.isEmpty()){
                pacote.setCliente(null);
            }else {
                pacote.setCliente(cliente.get());
            }
        }
        return pacote;
    }

}

