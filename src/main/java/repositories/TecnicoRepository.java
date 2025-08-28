package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer>{

}