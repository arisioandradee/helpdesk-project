package com.helpdeskturmaa.helpdesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.dto.TecnicoDTO;
import com.helpdeskturmaa.helpdesk.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.repositories.TecnicoRepository;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;

    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Técnico não encontrado! ID: " + id));
    }

    public List<TecnicoDTO> findAll() {
        List<Tecnico> list = repository.findAll();
        return list.stream().map(TecnicoDTO::new).collect(Collectors.toList());
    }

    public Tecnico create(TecnicoDTO dto) {
        dto.setId(null); 
        Tecnico newObj = new Tecnico(dto);
        return repository.save(newObj);
    }

    public Tecnico update(Integer id, TecnicoDTO dto) {
        Tecnico existing = findById(id);
        existing.setNome(dto.getNome());
        existing.setCpf(dto.getCpf());
        existing.setEmail(dto.getEmail());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        findById(id); 
        repository.deleteById(id);
    }
}
