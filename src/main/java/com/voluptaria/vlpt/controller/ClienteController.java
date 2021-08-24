package com.voluptaria.vlpt.controller;

import com.voluptaria.vlpt.dto.ClienteDTO;
import com.voluptaria.vlpt.dto.PacoteDTO;
import com.voluptaria.vlpt.exception.RegraNegocioException;
import com.voluptaria.vlpt.model.Cliente;
import com.voluptaria.vlpt.service.ClienteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@ApiOperation("Controller de Cliente")
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @GetMapping()
    public ResponseEntity getAll() {
      List<Cliente> clientes = service.getClientes();
      return ResponseEntity.ok(clientes.stream().map(ClienteDTO::createDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Optional<Cliente> cliente =  service.getClienteById(id);
        if(cliente.isEmpty()){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente não encontrado");
        }
        return ResponseEntity.ok(ClienteDTO.createDTO(cliente.get()));
    }

    @GetMapping("/{id}/pacotes")
    public ResponseEntity getPacotes(@PathVariable Long id){
        Optional<Cliente> cliente = service.getClienteById(id);
        if(cliente.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente não encontrado");
        }
        return ResponseEntity.ok(cliente.get().getPacotes()
                .stream().map(PacoteDTO::createDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody ClienteDTO clienteDTO){
         try {

            Cliente cliente = convertToModel(clienteDTO);
            Cliente clienteSalvo = service.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable Long id,@RequestBody ClienteDTO clienteDTO){
        if(service.getClienteById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não Encontrado");
        }
        try {
            Cliente cliente = convertToModel(clienteDTO);
            cliente.setId(id);
            service.update(cliente);
            return ResponseEntity.ok(cliente);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        Optional<Cliente> cliente = service.getClienteById(id);
        if(cliente.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente não encontrado");
        }
        service.delete(cliente.get());
        return ResponseEntity.noContent().build();
    }


    private Cliente convertToModel(ClienteDTO clienteDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(clienteDTO, Cliente.class);
    }



}
