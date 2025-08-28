package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}