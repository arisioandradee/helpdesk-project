package com.helpdeskturmaa.helpdesk.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.dto.ClienteDTO;
import com.helpdeskturmaa.helpdesk.service.ClienteService;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Integer id) {
        Cliente obj = service.findById(id);
        return ResponseEntity.ok().body(new ClienteDTO(obj));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<ClienteDTO> listDTO = service.findAll();
        return ResponseEntity.ok().body(listDTO);
    }

    // APENAS ADMIN PODE CRIAR CLIENTES
    @PreAuthorize("hasAnyRole('ADMIN')") 
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@RequestBody ClienteDTO dto) {
        Cliente newObj = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClienteDTO(newObj));
    }

    // APENAS ADMIN PODE ATUALIZAR OUTROS CLIENTES
    @PreAuthorize("hasAnyRole('ADMIN')") 
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Integer id, @RequestBody ClienteDTO dto) {
        Cliente updatedObj = service.update(id, dto);
        return ResponseEntity.ok().body(new ClienteDTO(updatedObj));
    }

    // APENAS ADMIN PODE DELETAR CLIENTES
    @PreAuthorize("hasAnyRole('ADMIN')") 
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}