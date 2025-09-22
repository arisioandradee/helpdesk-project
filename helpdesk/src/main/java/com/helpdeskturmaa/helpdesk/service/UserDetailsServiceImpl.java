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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
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