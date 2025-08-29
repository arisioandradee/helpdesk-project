package com.helpdeskturmaa.helpdesk.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.repositories.TecnicoRepository;

@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository repository;
	
	public Tecnico findById(Integer id) {
		
		Optional<Tecnico> obj = repository.findById(id);
			
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: "+ id));
		
	}

}