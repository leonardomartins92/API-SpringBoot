package com.voluptaria.vlpt.controller;

import com.voluptaria.vlpt.dto.DestinoDTO;
import com.voluptaria.vlpt.dto.EmpresaDTO;
import com.voluptaria.vlpt.dto.PassagemDTO;
import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Empresa;
import com.voluptaria.vlpt.service.EmpresaService;
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
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    @GetMapping()
    public ResponseEntity getAll() {
        List<Empresa> empresas = service.getEmpresas();
        return ResponseEntity.ok(empresas.stream().map(EmpresaDTO::createDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Empresa> empresa = service.getEmpresaById(id);
        if(!empresa.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empresa não encontrada");
        }
        return ResponseEntity.ok(EmpresaDTO.createDTO(empresa.get()));
    }

    @GetMapping("/{id}/destinos")
    public ResponseEntity getDestinos(@PathVariable Long id){
        Optional<Empresa> empresa = service.getEmpresaById(id);
        if(!empresa.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empresa não encontrada");
        }
        return ResponseEntity.ok(empresa.get().getDestinos()
                .stream().map(DestinoDTO::createDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}/passagens")
    public ResponseEntity getPassagens(@PathVariable Long id){
        Optional<Empresa> empresa = service.getEmpresaById(id);
        if(!empresa.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empresa não encontrada");
        }
        return ResponseEntity.ok(empresa.get().getPassagens()
                .stream().map(PassagemDTO::createDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody EmpresaDTO empresaDTO){
        try {
            Empresa empresa = convertToModel(empresaDTO);
            Empresa empresaSalvo = service.save(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalvo);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable Long id,@RequestBody EmpresaDTO empresaDTO){
        if(!service.getEmpresaById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não Encontrado");
        }
        try {
            Empresa empresa = convertToModel(empresaDTO);
            empresa.setId(id);
            service.update(empresa);
            return ResponseEntity.ok(empresa);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        Optional<Empresa> empresa = service.getEmpresaById(id);
        if(!empresa.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empresa não encontrado");
        }
        service.delete(empresa.get());
        return ResponseEntity.noContent().build();
    }

    private Empresa convertToModel(EmpresaDTO empresaDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(empresaDTO, Empresa.class);
    }
}
