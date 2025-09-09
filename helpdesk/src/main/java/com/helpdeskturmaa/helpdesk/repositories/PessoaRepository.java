package com.helpdeskturmaa.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helpdeskturmaa.helpdesk.domain.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer>{

}