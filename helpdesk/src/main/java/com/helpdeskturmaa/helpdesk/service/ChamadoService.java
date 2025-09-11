package com.helpdeskturmaa.helpdesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.dto.ChamadoDTO;
import com.helpdeskturmaa.helpdesk.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.repositories.ChamadoRepository;
import com.helpdeskturmaa.helpdesk.repositories.ClienteRepository;
import com.helpdeskturmaa.helpdesk.repositories.TecnicoRepository;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository repository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Chamado não encontrado! ID: " + id));
    }

    public List<ChamadoDTO> findAll() {
        List<Chamado> list = repository.findAll();
        return list.stream().map(ChamadoDTO::new).collect(Collectors.toList());
    }

    public Chamado create(ChamadoDTO dto) {
        dto.setId(null); 

        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnico())
                .orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! ID: " + dto.getTecnico()));
        Cliente cliente = clienteRepository.findById(dto.getCliente())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + dto.getCliente()));

        Chamado newObj = new Chamado(dto, tecnico, cliente);

        return repository.save(newObj);
    }

    public Chamado update(Integer id, ChamadoDTO dto) {
        Chamado existing = findById(id);

        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnico())
                .orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! ID: " + dto.getTecnico()));
        Cliente cliente = clienteRepository.findById(dto.getCliente())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + dto.getCliente()));

        existing.setTitulo(dto.getTitulo());
        existing.setObservacoes(dto.getObservacoes());
        existing.setPrioridade(dto.getPrioridade());
        existing.setStatus(dto.getStatus());
        existing.setTecnico(tecnico);
        existing.setCliente(cliente);

        return repository.save(existing);
    }

    public void delete(Integer id) {
        Chamado obj = findById(id);
        repository.delete(obj);
    }
}
