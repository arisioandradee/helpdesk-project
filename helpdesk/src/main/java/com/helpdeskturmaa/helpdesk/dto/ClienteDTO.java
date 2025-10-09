package com.helpdeskturmaa.helpdesk.dto;

import java.io.Serializable;
import com.helpdeskturmaa.helpdesk.domain.Cliente;

/**
 * Data Transfer Object (DTO) para a entidade {@link Cliente}.
 * * Esta classe é utilizada para transferência de dados do Cliente
 * entre a camada de serviço e a camada de apresentação, geralmente em requisições/respostas REST.
 * * Ela expõe apenas os dados necessários e pode incluir a senha ({@code senha})
 * para operações de criação ou atualização.
 * Implementa {@link Serializable} para permitir a serialização.
 */
public class ClienteDTO implements Serializable {

    /**
     * Identificador de serialização da classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * O ID (identificador) único do cliente.
     */
    private Integer id;

    /**
     * O nome completo do cliente.
     */
    private String nome;

    /**
     * O Cadastro de Pessoa Física (CPF) do cliente.
     */
    private String cpf;

    /**
     * O endereço de e-mail do cliente.
     */
    private String email;

    /**
     * A senha do cliente. Este campo é tipicamente usado para operações de criação ou atualização.
     */
    private String senha;

    /**
     * Construtor padrão (vazio) da classe ClienteDTO.
     */
    public ClienteDTO() {
        super();
    }
    
    /**
     * Construtor que recebe um objeto {@link Cliente} e o converte para ClienteDTO.
     * * Usado para ocultar a senha da entidade {@link Cliente} ao expor dados
     * para o frontend, pois o atributo {@code senha} não é copiado.
     *
     * @param obj O objeto Cliente original a ser convertido.
     */
    public ClienteDTO(Cliente obj) {
        super();
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.email = obj.getEmail();
        // A senha da entidade Cliente não é copiada para o DTO neste construtor,
        // garantindo que ela não seja exposta em consultas GET.
    }
    
    /**
     * Retorna o ID do cliente.
     *
     * @return o ID do cliente.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define o ID do cliente.
     *
     * @param id o novo ID do cliente.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retorna o nome do cliente.
     *
     * @return o nome do cliente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do cliente.
     *
     * @param nome o novo nome do cliente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CPF do cliente.
     *
     * @return o CPF do cliente.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do cliente.
     *
     * @param cpf o novo CPF do cliente.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Retorna o e-mail do cliente.
     *
     * @return o e-mail do cliente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do cliente.
     *
     * @param email o novo e-mail do cliente.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Retorna a senha do cliente.
     *
     * @return a senha do cliente (em texto puro ou codificada, dependendo do contexto de uso).
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do cliente.
     *
     * @param senha a nova senha do cliente.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}