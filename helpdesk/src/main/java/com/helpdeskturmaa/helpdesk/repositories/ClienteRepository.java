package com.helpdeskturmaa.helpdesk.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdeskturmaa.helpdesk.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByEmail(String email);
}