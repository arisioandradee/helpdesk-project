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

/**
 * Camada de Serviço responsável pela lógica de negócio e regras de acesso
 * relacionadas à entidade {@link Cliente}.
 */
@Service
public class ClienteService {

    /**
     * Injeção do repositório para acesso a dados de Cliente.
     */
    @Autowired
    private ClienteRepository repository;

    /**
     * Injeção do codificador de senha (BCryptPasswordEncoder).
     */
    @Autowired
    private PasswordEncoder encoder;
    
    /**
     * Injeção do serviço auxiliar para obter informações do usuário autenticado.
     */
    @Autowired 
    private UserService userService; 

    /**
     * Busca um cliente pelo seu ID.
     * * Aplica regras de autorização:
     * * Clientes comuns só podem visualizar seus próprios dados.
     * * ADMINs e TÉCNICO podem visualizar qualquer cliente.
     *
     * @param id O ID do cliente a ser buscado.
     * @return O objeto {@link Cliente} encontrado.
     * @throws ObjectNotFoundException Se o cliente não for encontrado.
     * @throws AuthorizationException Se o usuário logado não tiver permissão para acessar os dados.
     */
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

    /**
     * Retorna uma lista de todos os clientes.
     * * Aplica regras de autorização:
     * * ADMINs e TÉCNICOs retornam todos os clientes.
     * * Clientes comuns retornam apenas seus próprios dados em uma lista.
     *
     * @return Uma {@link List} de {@link ClienteDTO} contendo os clientes permitidos.
     * @throws AuthorizationException Se o usuário não estiver autenticado.
     */
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

    /**
     * Cria um novo cliente a partir de um DTO.
     * * Garante que o ID do DTO seja nulo para forçar a criação.
     * * Valida a integridade dos dados (CPF e E-mail não repetidos).
     * * Codifica a senha antes de persistir.
     *
     * @param objDTO O {@link ClienteDTO} com os dados para criação.
     * @return O objeto {@link Cliente} recém-criado e persistido.
     * @throws DataIntegrityViolationException Se o CPF ou E-mail já estiverem cadastrados.
     */
    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null); 
        validarCPFEEmail(objDTO, null); 
        
        Cliente newObj = new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getCpf(), objDTO.getEmail(), encoder.encode(objDTO.getSenha()));
        return repository.save(newObj);
    }

    /**
     * Atualiza os dados de um cliente existente.
     * * Primeiro busca o cliente existente (via {@code findById}, que já valida o acesso).
     * * Valida se houve alteração no CPF ou E-mail e verifica a integridade.
     * * Codifica a nova senha se for fornecida, caso contrário, mantém a senha antiga.
     *
     * @param id O ID do cliente a ser atualizado.
     * @param dto O {@link ClienteDTO} com os novos dados.
     * @return O objeto {@link Cliente} atualizado.
     * @throws ObjectNotFoundException Se o cliente não for encontrado.
     * @throws DataIntegrityViolationException Se o novo CPF ou E-mail já estiverem cadastrados em outro usuário.
     */
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

    /**
     * Deleta um cliente pelo seu ID.
     * * Primeiro busca o cliente (via {@code findById}) para validar sua existência.
     *
     * @param id O ID do cliente a ser deletado.
     * @throws ObjectNotFoundException Se o cliente não for encontrado.
     * @throws AuthorizationException Se o usuário logado não tiver permissão para a busca inicial.
     */
    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
    
    /**
     * Método auxiliar para validar se o CPF e o E-mail de um DTO já existem no sistema.
     *
     * @param dto O {@link ClienteDTO} contendo os dados a serem validados.
     * @param id O ID do cliente, se for uma operação de atualização ({@code null} se for criação).
     * @throws DataIntegrityViolationException Se o CPF ou E-mail já estiverem cadastrados em outro usuário (ID diferente).
     */
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