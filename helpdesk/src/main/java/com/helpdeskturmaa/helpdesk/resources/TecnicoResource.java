package com.helpdeskturmaa.helpdesk.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.dto.TecnicoDTO;
import com.helpdeskturmaa.helpdesk.service.TecnicoService;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller REST que gerencia as operações HTTP (CRUD) para a entidade {@link Tecnico}.
 * * Mapeia as requisições para o endpoint base "/tecnicos".
 * * As operações de atualização e exclusão são restritas ao perfil ADMIN.
 */
@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoResource {

    /**
     * Injeção do serviço responsável pela lógica de negócio dos Técnicos.
     */
    @Autowired
    private TecnicoService service;

    /**
     * Endpoint para buscar um Técnico específico pelo seu ID.
     *
     * @param id O ID do técnico a ser buscado.
     * @return {@link ResponseEntity} com o {@link TecnicoDTO} correspondente e status HTTP 200 (OK).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
        Tecnico obj = service.findById(id);
        return ResponseEntity.ok().body(new TecnicoDTO(obj));
    }

    /**
     * Endpoint para buscar todos os Técnicos registrados no sistema.
     *
     * @return {@link ResponseEntity} com uma {@link List} de {@link TecnicoDTO} e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<TecnicoDTO>> findAll() {
        List<TecnicoDTO> listDTO = service.findAll();
        return ResponseEntity.ok().body(listDTO);
    }

    /**
     * Endpoint para criar um novo Técnico.
     *
     * @param objDTO O {@link TecnicoDTO} contendo os dados do novo técnico.
     * @return {@link ResponseEntity} com o {@link TecnicoDTO} criado, status HTTP 201 (Created)
     * e o URI do novo recurso no cabeçalho 'Location'.
     */
    @PostMapping
    public ResponseEntity<TecnicoDTO> create(@RequestBody TecnicoDTO objDTO) {
        Tecnico newObj = service.create(objDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(new TecnicoDTO(newObj));
    }

    /**
     * Endpoint para atualizar os dados de um Técnico existente.
     * * Requer que o usuário autenticado possua a autoridade 'ROLE_ADMIN'.
     *
     * @param id O ID do técnico a ser atualizado.
     * @param objDTO O {@link TecnicoDTO} com os novos dados.
     * @return {@link ResponseEntity} com o {@link TecnicoDTO} atualizado e status HTTP 200 (OK).
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    @PutMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @RequestBody TecnicoDTO objDTO) {
        Tecnico updatedObj = service.update(id, objDTO);
        return ResponseEntity.ok().body(new TecnicoDTO(updatedObj));
    }

    /**
     * Endpoint para deletar um Técnico pelo seu ID.
     * * Requer que o usuário autenticado possua a autoridade 'ROLE_ADMIN'.
     *
     * @param id O ID do técnico a ser deletado.
     * @return {@link ResponseEntity} com status HTTP 204 (No Content), indicando sucesso na remoção.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}