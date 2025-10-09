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

/**
 * Controller REST que gerencia as operações HTTP (CRUD) para a entidade {@link Cliente}.
 * * Mapeia as requisições para o endpoint base "/clientes".
 * * As operações de criação, atualização e exclusão são restritas ao perfil ADMIN.
 */
@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    /**
     * Injeção do serviço responsável pela lógica de negócio dos Clientes.
     */
    @Autowired
    private ClienteService service;

    /**
     * Endpoint para buscar um Cliente específico pelo seu ID.
     *
     * @param id O ID do cliente a ser buscado.
     * @return {@link ResponseEntity} com o {@link ClienteDTO} correspondente e status HTTP 200 (OK).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Integer id) {
        Cliente obj = service.findById(id);
        return ResponseEntity.ok().body(new ClienteDTO(obj));
    }

    /**
     * Endpoint para buscar todos os Clientes registrados no sistema.
     *
     * @return {@link ResponseEntity} com uma {@link List} de {@link ClienteDTO} e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<ClienteDTO> listDTO = service.findAll();
        return ResponseEntity.ok().body(listDTO);
    }

    // APENAS ADMIN PODE CRIAR CLIENTES
    /**
     * Endpoint para criar um novo Cliente.
     * * Requer que o usuário autenticado possua a autoridade 'ROLE_ADMIN'.
     *
     * @param dto O {@link ClienteDTO} contendo os dados do novo cliente.
     * @return {@link ResponseEntity} com o {@link ClienteDTO} criado, status HTTP 201 (Created)
     * e o URI do novo recurso no cabeçalho 'Location'.
     */
    @PreAuthorize("hasAnyRole('ADMIN')") 
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@RequestBody ClienteDTO dto) {
        Cliente newObj = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClienteDTO(newObj));
    }

    // APENAS ADMIN PODE ATUALIZAR OUTROS CLIENTES
    /**
     * Endpoint para atualizar os dados de um Cliente existente.
     * * Requer que o usuário autenticado possua a autoridade 'ROLE_ADMIN'.
     *
     * @param id O ID do cliente a ser atualizado.
     * @param dto O {@link ClienteDTO} com os novos dados.
     * @return {@link ResponseEntity} com o {@link ClienteDTO} atualizado e status HTTP 200 (OK).
     */
    @PreAuthorize("hasAnyRole('ADMIN')") 
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Integer id, @RequestBody ClienteDTO dto) {
        Cliente updatedObj = service.update(id, dto);
        return ResponseEntity.ok().body(new ClienteDTO(updatedObj));
    }

    // APENAS ADMIN PODE DELETAR CLIENTES
    /**
     * Endpoint para deletar um Cliente pelo seu ID.
     * * Requer que o usuário autenticado possua a autoridade 'ROLE_ADMIN'.
     *
     * @param id O ID do cliente a ser deletado.
     * @return {@link ResponseEntity} com status HTTP 204 (No Content), indicando sucesso na remoção.
     */
    @PreAuthorize("hasAnyRole('ADMIN')") 
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}