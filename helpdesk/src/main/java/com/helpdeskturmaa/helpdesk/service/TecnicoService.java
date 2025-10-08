package com.helpdeskturmaa.helpdesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.dto.TecnicoDTO;
import com.helpdeskturmaa.helpdesk.repositories.TecnicoRepository;
import com.helpdeskturmaa.helpdesk.resources.exceptions.AuthorizationException;
import com.helpdeskturmaa.helpdesk.resources.exceptions.DataIntegrityViolationException;
import com.helpdeskturmaa.helpdesk.resources.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.security.UserSS;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;
    
    @Autowired
    private PasswordEncoder encoder;

    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Técnico não encontrado! ID: " + id));
    }

    public List<TecnicoDTO> findAll() {
        List<Tecnico> list = repository.findAll();
        return list.stream().map(TecnicoDTO::new).collect(Collectors.toList());
    }

    public Tecnico create(TecnicoDTO dto) {
        dto.setId(null); 
        validarDados(dto);
        
        dto.setSenha(encoder.encode(dto.getSenha())); 
        
        Tecnico newObj = new Tecnico(dto);
        return repository.save(newObj);
    }

    public Tecnico update(Integer id, TecnicoDTO dto) {
        Tecnico existing = findById(id);
        
        if (!dto.getCpf().equals(existing.getCpf()) || !dto.getEmail().equals(existing.getEmail())) {
            validarDados(dto); //se for alterado, eu consigo validar
        }
        
        existing.setNome(dto.getNome());
        existing.setCpf(dto.getCpf());
        existing.setEmail(dto.getEmail());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        UserSS usuarioLogado = UserService.authenticated();

        if (usuarioLogado == null || !usuarioLogado.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AuthorizationException("Acesso negado! Apenas administradores podem deletar técnicos.");
       }

        findById(id); 
        repository.deleteById(id);
    }
    
    private void validarDados(TecnicoDTO dto) {
        Optional<Tecnico> objCpf = repository.findByCpf(dto.getCpf());

        if (objCpf.isPresent() && !objCpf.get().getId().equals(dto.getId())) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        Optional<Tecnico> objEmail = repository.findByEmail(dto.getEmail());

        if (objEmail.isPresent() && !objEmail.get().getId().equals(dto.getId())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }

}
