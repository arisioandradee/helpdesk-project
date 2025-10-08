package com.helpdeskturmaa.helpdesk.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.domain.enums.Status;
import com.helpdeskturmaa.helpdesk.dto.ChamadoDTO;
import com.helpdeskturmaa.helpdesk.repositories.ChamadoRepository;
import com.helpdeskturmaa.helpdesk.repositories.ClienteRepository;
import com.helpdeskturmaa.helpdesk.repositories.TecnicoRepository;
import com.helpdeskturmaa.helpdesk.resources.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.resources.exceptions.AuthorizationException; 
import com.helpdeskturmaa.helpdesk.security.UserSS; 

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository repository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    private void validarPropriedade(Chamado chamado) {
    	UserSS usuarioLogado = UserService.authenticated();
        
        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }
        
        if (usuarioLogado.hasRole("ADMIN") || usuarioLogado.hasRole("TECNICO")) {
            return;
        }

        if (!chamado.getCliente().getId().equals(usuarioLogado.getId())) {
            throw new AuthorizationException("Acesso negado! Você só pode visualizar seus próprios chamados.");
        }
    }


    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        Chamado chamado = obj.orElseThrow(() -> new ObjectNotFoundException("Chamado não encontrado! ID: " + id));
        
        validarPropriedade(chamado); 
        
        return chamado;
    }

    public List<ChamadoDTO> findAll() {
        UserSS usuarioLogado = UserService.authenticated();

        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }

        List<Chamado> list;

        if (usuarioLogado.hasRole("ADMIN")) {
            list = repository.findAll();
        } 
        else if (usuarioLogado.hasRole("TECNICO")) {
            list = repository.findByTecnicoId(usuarioLogado.getId());
        } 
        else {
            list = repository.findByClienteId(usuarioLogado.getId());
        }

        return list.stream().map(ChamadoDTO::new).collect(Collectors.toList());
    }


    public Chamado create(ChamadoDTO dto) {
        dto.setId(null);
        UserSS usuarioLogado = UserService.authenticated();

        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnico())
                .orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! ID: " + dto.getTecnico()));

        if (!(tecnico instanceof Tecnico)) {
             throw new ObjectNotFoundException("ID " + dto.getTecnico() + " não representa um Técnico válido (Erro de Mapeamento).");
        }

        Cliente cliente = clienteRepository.findById(dto.getCliente())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + dto.getCliente()));

        if (!(cliente instanceof Cliente)) {
             throw new ObjectNotFoundException("ID " + dto.getCliente() + " não representa um Cliente válido (Erro de Mapeamento).");
        }

        Chamado newObj = new Chamado(dto, tecnico, cliente);
        return repository.save(newObj);
    }

    public Chamado update(Integer id, ChamadoDTO dto) {
        Chamado existing = findById(id);
        UserSS usuarioLogado = UserService.authenticated();
        
        if (dto.getStatus() != null && !dto.getStatus().equals(existing.getStatus())) {
            
            if (dto.getStatus() == Status.ENCERRADO) { 
                existing.setDataFechamento(LocalDate.now()); 
            } 
            // Se o chamado estava ENCERRADO e agora está ANDAMENTO, limpamos a data de fechamento
            else if (existing.getStatus() == Status.ENCERRADO) {
                 existing.setDataFechamento(null);
            }
        }
        
        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnico())
                .orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! ID: " + dto.getTecnico()));
        Cliente cliente = clienteRepository.findById(dto.getCliente())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + dto.getCliente()));

        existing.setTitulo(dto.getTitulo());
        existing.setObservacoes(dto.getObservacoes());
        existing.setPrioridade(dto.getPrioridade());

        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        } else {
             existing.setStatus(existing.getStatus()); 
        }
        
        existing.setTecnico(tecnico);
        existing.setCliente(cliente);

        return repository.save(existing);
    }

    public void delete(Integer id) {
        Chamado obj = findById(id);
        UserSS usuarioLogado = UserService.authenticated();

        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }
        if (!usuarioLogado.hasRole("ADMIN")) {
            throw new AuthorizationException("Acesso negado. Apenas administradores podem excluir chamados.");
        }
        repository.delete(obj);
    }
}