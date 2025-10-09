package com.helpdeskturmaa.helpdesk.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.domain.enums.Status;
import com.helpdeskturmaa.helpdesk.dto.ChamadoDTO;
import com.helpdeskturmaa.helpdesk.repositories.ChamadoRepository;
import com.helpdeskturmaa.helpdesk.repositories.ClienteRepository;
import com.helpdeskturmaa.helpdesk.repositories.TecnicoRepository;
import com.helpdeskturmaa.helpdesk.resources.exceptions.ObjectNotFoundException;
import com.helpdeskturmaa.helpdesk.resources.exceptions.AuthorizationException; 
import com.helpdeskturmaa.helpdesk.security.UserSS; 

/**
 * Camada de Serviço responsável pela lógica de negócio e regras de acesso
 * relacionadas à entidade {@link Chamado}.
 */
@Service
public class ChamadoService {

    /**
     * Injeção do repositório para acesso a dados de Chamado.
     */
    @Autowired
    private ChamadoRepository repository;

    /**
     * Injeção do repositório para acesso a dados de Técnico.
     */
    @Autowired
    private TecnicoRepository tecnicoRepository;

    /**
     * Injeção do repositório para acesso a dados de Cliente.
     */
    @Autowired
    private ClienteRepository clienteRepository;


    /**
     * Realiza a validação de propriedade do chamado, garantindo que apenas
     * o cliente que abriu, o técnico relacionado ou um ADMIN possam visualizá-lo.
     *
     * @param chamado O objeto Chamado a ser validado.
     * @throws AuthorizationException Se o usuário não estiver autenticado ou não tiver permissão para acessar o chamado.
     */
    private void validarPropriedade(Chamado chamado) {
    	UserSS usuarioLogado = UserService.authenticated();
        
        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }
        
        if (usuarioLogado.hasRole("ADMIN") || usuarioLogado.hasRole("TECNICO")) {
            return;
        }

        if (!chamado.getCliente().getId().equals(usuarioLogado.getId())) {
            throw new AuthorizationException("Acesso negado! Você só pode visualizar seus próprios chamados.");
        }
    }


    /**
     * Busca um chamado pelo seu ID e valida se o usuário logado tem permissão para acessá-lo.
     *
     * @param id O ID do chamado a ser buscado.
     * @return O objeto {@link Chamado} encontrado.
     * @throws ObjectNotFoundException Se o chamado não for encontrado.
     * @throws AuthorizationException Se o usuário não tiver permissão para acessar o chamado.
     */
    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        Chamado chamado = obj.orElseThrow(() -> new ObjectNotFoundException("Chamado não encontrado! ID: " + id));
        
        validarPropriedade(chamado); 
        
        return chamado;
    }

    /**
     * Busca todos os chamados, aplicando regras de filtro baseadas no perfil do usuário logado.
     * * ADMIN: Retorna todos os chamados.
     * * TÉCNICO: Retorna apenas os chamados atribuídos a ele.
     * * CLIENTE: Retorna apenas os chamados abertos por ele.
     *
     * @return Uma {@link List} de {@link ChamadoDTO} contendo os chamados permitidos.
     * @throws AuthorizationException Se o usuário não estiver autenticado.
     */
    public List<ChamadoDTO> findAll() {
        UserSS usuarioLogado = UserService.authenticated();

        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }

        List<Chamado> list;

        if (usuarioLogado.hasRole("ADMIN")) {
            list = repository.findAll();
        } 
        else if (usuarioLogado.hasRole("TECNICO")) {
            list = repository.findByTecnicoId(usuarioLogado.getId());
        } 
        else {
            list = repository.findByClienteId(usuarioLogado.getId());
        }

        return list.stream().map(ChamadoDTO::new).collect(Collectors.toList());
    }


    /**
     * Cria um novo chamado a partir de um DTO.
     * * Garante que o ID do DTO seja nulo para forçar a criação de um novo registro.
     * * Valida a existência do Técnico e do Cliente referenciados no DTO.
     *
     * @param dto O {@link ChamadoDTO} com os dados para criação.
     * @return O objeto {@link Chamado} recém-criado e persistido.
     * @throws ObjectNotFoundException Se o Técnico ou Cliente referenciado não for encontrado.
     */
    public Chamado create(ChamadoDTO dto) {
        dto.setId(null);
        UserSS usuarioLogado = UserService.authenticated();

        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnico())
                .orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! ID: " + dto.getTecnico()));

        if (!(tecnico instanceof Tecnico)) {
             throw new ObjectNotFoundException("ID " + dto.getTecnico() + " não representa um Técnico válido (Erro de Mapeamento).");
        }

        Cliente cliente = clienteRepository.findById(dto.getCliente())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + dto.getCliente()));

        if (!(cliente instanceof Cliente)) {
             throw new ObjectNotFoundException("ID " + dto.getCliente() + " não representa um Cliente válido (Erro de Mapeamento).");
        }

        Chamado newObj = new Chamado(dto, tecnico, cliente);
        return repository.save(newObj);
    }

    /**
     * Atualiza um chamado existente.
     * * Busca o chamado existente pelo ID e o valida para acesso (via {@code findById}).
     * * Atualiza a data de fechamento se o status for alterado para ENCERRADO.
     * * Valida a existência das referências de Técnico e Cliente.
     *
     * @param id O ID do chamado a ser atualizado.
     * @param dto O {@link ChamadoDTO} com os dados de atualização.
     * @return O objeto {@link Chamado} atualizado.
     * @throws ObjectNotFoundException Se o chamado, Técnico ou Cliente não for encontrado.
     */
    public Chamado update(Integer id, ChamadoDTO dto) {
        Chamado existing = findById(id);
        UserSS usuarioLogado = UserService.authenticated();
        
        if (dto.getStatus() != null && !dto.getStatus().equals(existing.getStatus())) {
            
            if (dto.getStatus() == Status.ENCERRADO) { 
                existing.setDataFechamento(LocalDate.now()); 
            } 
            // Se o chamado estava ENCERRADO e agora está ANDAMENTO, limpamos a data de fechamento
            else if (existing.getStatus() == Status.ENCERRADO) {
                 existing.setDataFechamento(null);
            }
        }
        
        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnico())
                .orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! ID: " + dto.getTecnico()));
        Cliente cliente = clienteRepository.findById(dto.getCliente())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + dto.getCliente()));

        existing.setTitulo(dto.getTitulo());
        existing.setObservacoes(dto.getObservacoes());
        existing.setPrioridade(dto.getPrioridade());

        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        } else {
             existing.setStatus(existing.getStatus()); 
        }
        
        existing.setTecnico(tecnico);
        existing.setCliente(cliente);

        return repository.save(existing);
    }

    /**
     * Deleta um chamado pelo seu ID.
     * * Requer que o usuário logado seja um ADMIN para executar a exclusão.
     *
     * @param id O ID do chamado a ser deletado.
     * @throws ObjectNotFoundException Se o chamado não for encontrado.
     * @throws AuthorizationException Se o usuário não for um ADMIN ou não estiver autenticado.
     */
    public void delete(Integer id) {
        Chamado obj = findById(id);
        UserSS usuarioLogado = UserService.authenticated();

        if (usuarioLogado == null) {
            throw new AuthorizationException("Usuário não autenticado.");
        }
        if (!usuarioLogado.hasRole("ADMIN")) {
            throw new AuthorizationException("Acesso negado. Apenas administradores podem excluir chamados.");
        }
        repository.delete(obj);
    }
}