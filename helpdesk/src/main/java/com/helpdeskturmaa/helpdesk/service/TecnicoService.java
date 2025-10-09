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

/**
 * Camada de Serviço responsável pela lógica de negócio e regras de acesso
 * relacionadas à entidade {@link Tecnico}.
 */
@Service
public class TecnicoService {

    /**
     * Injeção do repositório para acesso a dados de Técnico.
     */
    @Autowired
    private TecnicoRepository repository;
    
    /**
     * Injeção do codificador de senha (BCryptPasswordEncoder).
     */
    @Autowired
    private PasswordEncoder encoder;

    /**
     * Busca um técnico pelo seu ID.
     *
     * @param id O ID do técnico a ser buscado.
     * @return O objeto {@link Tecnico} encontrado.
     * @throws ObjectNotFoundException Se o técnico não for encontrado.
     */
    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Técnico não encontrado! ID: " + id));
    }

    /**
     * Retorna uma lista de todos os técnicos registrados no sistema.
     *
     * @return Uma {@link List} de {@link TecnicoDTO} contendo todos os técnicos.
     */
    public List<TecnicoDTO> findAll() {
        List<Tecnico> list = repository.findAll();
        return list.stream().map(TecnicoDTO::new).collect(Collectors.toList());
    }

    /**
     * Cria um novo técnico a partir de um DTO.
     * * Garante que o ID do DTO seja nulo para forçar a criação.
     * * Valida a integridade dos dados (CPF e E-mail não repetidos).
     * * Codifica a senha antes de persistir.
     *
     * @param dto O {@link TecnicoDTO} com os dados para criação.
     * @return O objeto {@link Tecnico} recém-criado e persistido.
     * @throws DataIntegrityViolationException Se o CPF ou E-mail já estiverem cadastrados.
     */
    public Tecnico create(TecnicoDTO dto) {
        dto.setId(null); 
        validarDados(dto);
        
        dto.setSenha(encoder.encode(dto.getSenha())); 
        
        Tecnico newObj = new Tecnico(dto);
        return repository.save(newObj);
    }

    /**
     * Atualiza os dados de um técnico existente.
     * * Busca o técnico existente (via {@code findById}).
     * * Valida a integridade dos dados se o CPF ou E-mail foram alterados.
     * * Nota: A lógica de atualização de senha não está presente neste método, apenas dados básicos são alterados.
     *
     * @param id O ID do técnico a ser atualizado.
     * @param dto O {@link TecnicoDTO} com os novos dados.
     * @return O objeto {@link Tecnico} atualizado.
     * @throws ObjectNotFoundException Se o técnico não for encontrado.
     * @throws DataIntegrityViolationException Se o novo CPF ou E-mail já estiverem cadastrados em outro usuário.
     */
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

    /**
     * Deleta um técnico pelo seu ID.
     * * Requer que o usuário logado possua a autoridade 'ROLE_ADMIN' para executar a exclusão.
     *
     * @param id O ID do técnico a ser deletado.
     * @throws AuthorizationException Se o usuário logado não for um ADMIN.
     * @throws ObjectNotFoundException Se o técnico não for encontrado.
     */
    public void delete(Integer id) {
        UserSS usuarioLogado = UserService.authenticated();

        if (usuarioLogado == null || !usuarioLogado.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AuthorizationException("Acesso negado! Apenas administradores podem deletar técnicos.");
       }

        findById(id); 
        repository.deleteById(id);
    }
    
    /**
     * Método auxiliar para validar se o CPF e o E-mail de um DTO já existem no sistema
     * (e não pertencem ao mesmo usuário, no caso de uma atualização).
     *
     * @param dto O {@link TecnicoDTO} contendo os dados a serem validados.
     * @throws DataIntegrityViolationException Se o CPF ou E-mail já estiverem cadastrados em outro técnico.
     */
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