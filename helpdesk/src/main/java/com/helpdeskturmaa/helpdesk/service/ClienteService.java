package com.helpdeskturmaa.helpdesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.dto.ClienteDTO;
import com.helpdeskturmaa.helpdesk.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.repositories.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Cliente findById(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + id));
    }

    public List<ClienteDTO> findAll() {
        List<Cliente> list = repository.findAll();
        return list.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

    public Cliente create(ClienteDTO dto) {
        dto.setId(null); 
        Cliente newObj = new Cliente(dto); 
        return repository.save(newObj);
    }

    public Cliente update(Integer id, ClienteDTO dto) {
        Cliente existing = findById(id);
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
