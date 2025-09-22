package com.helpdeskturmaa.helpdesk.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helpdeskturmaa.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer>{
    Optional<Tecnico> findByCpf(String cpf);
    Optional<Tecnico> findByEmail(String email);
}