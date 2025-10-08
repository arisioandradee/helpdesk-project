package com.helpdeskturmaa.helpdesk.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdeskturmaa.helpdesk.domain.Chamado;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Integer>{
	List<Chamado> findByClienteId(Integer id); 
	List<Chamado> findByTecnicoId(Integer id);
}	