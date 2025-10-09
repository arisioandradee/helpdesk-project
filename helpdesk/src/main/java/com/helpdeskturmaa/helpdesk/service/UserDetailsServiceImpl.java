package com.helpdeskturmaa.helpdesk.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Pessoa;
import com.helpdeskturmaa.helpdesk.repositories.PessoaRepository;
import com.helpdeskturmaa.helpdesk.security.UserSS;

/**
 * Serviço de implementação do {@link UserDetailsService} do Spring Security.
 * * Responsável por carregar um usuário ({@link Pessoa}) pelo seu nome de usuário (e-mail)
 * e retornar um objeto {@link UserDetails} (aqui representado por {@link UserSS})
 * para o processo de autenticação.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/**
	 * Injeção do repositório genérico para a entidade Pessoa, utilizada para buscar o usuário.
	 */
	@Autowired
	private PessoaRepository pessoaRepository;
	
    /**
     * Carrega os dados de um usuário pelo seu nome de usuário (e-mail) para autenticação.
     *
     * @param email O e-mail do usuário, que serve como nome de usuário.
     * @return Um objeto {@link UserDetails} (instância de {@link UserSS}) contendo as informações e perfis do usuário.
     * @throws UsernameNotFoundException Se nenhum usuário for encontrado com o e-mail fornecido.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Pessoa> pessoa = pessoaRepository.findByEmail(email);

        if (pessoa.isPresent()) {
            return new UserSS(
                    pessoa.get().getId(),
                    pessoa.get().getEmail(),
                    pessoa.get().getSenha(),
                    pessoa.get().getPerfis()
            );
        }

        throw new UsernameNotFoundException(email);
    }
}