package com.helpdeskturmaa.helpdesk.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.dto.ClienteDTO;
import com.helpdeskturmaa.helpdesk.repositories.ClienteRepository;
import com.helpdeskturmaa.helpdesk.resources.exceptions.AuthorizationException;
import com.helpdeskturmaa.helpdesk.resources.exceptions.DataIntegrityViolationException;
import com.helpdeskturmaa.helpdesk.resources.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.security.UserSS;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired 
    private UserService userService; 

    public Cliente findById(Integer id) {
        UserSS usuarioLogado = UserService.authenticated();
        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }
        if (usuarioLogado.hasRole("CLIENTE") && 
            !usuarioLogado.hasRole("ADMIN") && 
            !usuarioLogado.hasRole("TECNICO") && 
            !usuarioLogado.getId().equals(id)) {
            
            throw new AuthorizationException("Acesso negado! Cliente só pode ver seus próprios dados.");
        }

        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + id));
    }

    public List<ClienteDTO> findAll() {
        UserSS usuarioLogado = UserService.authenticated();

        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }
        if (usuarioLogado.hasRole("CLIENTE") && 
            !usuarioLogado.hasRole("ADMIN") && 
            !usuarioLogado.hasRole("TECNICO")) {
            Cliente cliente = findById(usuarioLogado.getId());
            return Arrays.asList(new ClienteDTO(cliente));
        }
        List<Cliente> list = repository.findAll();
        return list.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

    public Cliente create(ClienteDTO dto) {
        dto.setId(null); 
        validarDados(dto);
        
        dto.setSenha(encoder.encode(dto.getSenha())); 
        
        Cliente newObj = new Cliente(dto); 
        return repository.save(newObj);
    }

    public Cliente update(Integer id, ClienteDTO dto) {
        Cliente existing = findById(id);
        
        if (!dto.getCpf().equals(existing.getCpf()) || !dto.getEmail().equals(existing.getEmail())) {
            validarDados(dto); 
        }
        
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            existing.setSenha(encoder.encode(dto.getSenha()));
        } else {
             dto.setSenha(existing.getSenha());
        }
        
        existing.setNome(dto.getNome());
        existing.setCpf(dto.getCpf());
        existing.setEmail(dto.getEmail());
        
        return repository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
    
    private void validarDados(ClienteDTO dto) {
        Optional<Cliente> objCpf = repository.findByCpf(dto.getCpf());
        if(objCpf.isPresent() && !objCpf.get().getId().equals(dto.getId())) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        Optional<Cliente> objEmail = repository.findByEmail(dto.getEmail());
        if(objEmail.isPresent() && !objEmail.get().getId().equals(dto.getId())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }
}