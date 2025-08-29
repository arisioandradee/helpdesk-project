package com.helpdeskturmaa.helpdesk.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.repositories.ChamadoRepository;

@Service
public class ChamadoService {
	@Autowired
	private ChamadoRepository repository;
	
	public Chamado findById(Integer id) {
		
		Optional<Chamado> obj = repository.findById(id);
			
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: "+ id));
	}
}