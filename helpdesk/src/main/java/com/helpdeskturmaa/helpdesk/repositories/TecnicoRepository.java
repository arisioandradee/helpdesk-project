package com.helpdeskturmaa.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helpdeskturmaa.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer>{

}