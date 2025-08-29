package com.helpdeskturmaa.helpdesk.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired; // Importe essa anotação
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.repositories.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
	private ClienteRepository repository;
	
	public Cliente findById(Integer id) {
		
		Optional<Cliente> obj = repository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: "+ id));
	}
}