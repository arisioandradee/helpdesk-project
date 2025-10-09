package com.helpdeskturmaa.helpdesk.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.dto.ChamadoDTO;
import com.helpdeskturmaa.helpdesk.service.ChamadoService;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller REST que gerencia as operações HTTP (CRUD) para a entidade {@link Chamado}.
 * * Mapeia as requisições para o endpoint base "/chamados".
 */
@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResource {

    /**
     * Injeção do serviço responsável pela lógica de negócio dos Chamados.
     */
    @Autowired
    private ChamadoService service;

    /**
     * Endpoint para buscar um Chamado específico pelo seu ID.
     *
     * @param id O ID do chamado a ser buscado.
     * @return {@link ResponseEntity} com o {@link ChamadoDTO} correspondente e status HTTP 200 (OK).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id) {
        Chamado obj = service.findById(id);
        return ResponseEntity.ok().body(new ChamadoDTO(obj));
    }

    /**
     * Endpoint para buscar todos os Chamados registrados no sistema.
     *
     * @return {@link ResponseEntity} com uma {@link List} de {@link ChamadoDTO} e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> findAll() {
        List<ChamadoDTO> listDTO = service.findAll();
        return ResponseEntity.ok().body(listDTO);
    }

    /**
     * Endpoint para criar um novo Chamado.
     *
     * @param dto O {@link ChamadoDTO} contendo os dados do novo chamado.
     * @return {@link ResponseEntity} com o {@link ChamadoDTO} criado, status HTTP 201 (Created)
     * e o URI do novo recurso no cabeçalho 'Location'.
     */
    @PostMapping
    public ResponseEntity<ChamadoDTO> create(@RequestBody ChamadoDTO dto) {
        Chamado newObj = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(new ChamadoDTO(newObj));
    }

    /**
     * Endpoint para atualizar os dados de um Chamado existente.
     *
     * @param id O ID do chamado a ser atualizado.
     * @param dto O {@link ChamadoDTO} com os novos dados.
     * @return {@link ResponseEntity} com o {@link ChamadoDTO} atualizado e status HTTP 200 (OK).
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> update(@PathVariable Integer id, @RequestBody ChamadoDTO dto) {
        Chamado updatedObj = service.update(id, dto);
        return ResponseEntity.ok().body(new ChamadoDTO(updatedObj));
    }

    /**
     * Endpoint para deletar um Chamado pelo seu ID.
     *
     * @param id O ID do chamado a ser deletado.
     * @return {@link ResponseEntity} com status HTTP 204 (No Content), indicando sucesso na remoção.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}