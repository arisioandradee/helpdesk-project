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

    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null); 
        validarCPFEEmail(objDTO, null); 
        
        Cliente newObj = new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getCpf(), objDTO.getEmail(), encoder.encode(objDTO.getSenha()));
        return repository.save(newObj);
    }

    public Cliente update(Integer id, ClienteDTO dto) {
        dto.setId(id);
        Cliente existing = findById(id);
        
        if (!dto.getCpf().equals(existing.getCpf()) || !dto.getEmail().equals(existing.getEmail())) {
            validarCPFEEmail(dto, id); 
        }

        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            existing.setSenha(encoder.encode(dto.getSenha()));
        } else {
            dto.setSenha(existing.getSenha());
        }

        existing.setNome(dto.getNome());
        existing.setCpf(dto.getCpf());
        existing.setEmail(dto.getEmail());
        
        try {
            return repository.save(existing);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
             throw new DataIntegrityViolationException("CPF ou Email já cadastrados no sistema.");
        }
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
    
    private void validarCPFEEmail(ClienteDTO dto, Integer id) {
        Optional<Cliente> clienteComCpf = repository.findByCpf(dto.getCpf());
        if (clienteComCpf.isPresent() && !clienteComCpf.get().getId().equals(id)) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        Optional<Cliente> clienteComEmail = repository.findByEmail(dto.getEmail());
        if (clienteComEmail.isPresent() && !clienteComEmail.get().getId().equals(id)) {
            throw new DataIntegrityViolationException("Email já cadastrado no sistema!");
        }
    }
}