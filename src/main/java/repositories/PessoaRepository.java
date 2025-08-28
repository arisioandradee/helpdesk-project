package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer>{

}