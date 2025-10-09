package com.helpdeskturmaa.helpdesk.dto;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) para representar as credenciais de autenticação
 * de um usuário (login).
 * * Utilizada especificamente para receber o e-mail e a senha durante o processo de login
 * na aplicação.
 * Implementa {@link Serializable} para permitir a serialização.
 */
public class CredenciaisDTO implements Serializable {

    /**
     * Identificador de serialização da classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * O e-mail (username) utilizado para o login.
     */
    private String email;

    /**
     * A senha fornecida pelo usuário.
     */
    private String senha;

    /**
     * Construtor padrão (vazio) da classe CredenciaisDTO.
     */
    public CredenciaisDTO() {
        super();
    }

    /**
     * Construtor que inicializa o DTO com o e-mail e a senha fornecidos.
     *
     * @param email O e-mail (username) do usuário.
     * @param senha A senha do usuário.
     */
    public CredenciaisDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    /**
     * Retorna o e-mail (username) para o login.
     *
     * @return o e-mail do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail (username) para o login.
     *
     * @param email o novo e-mail.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna a senha fornecida para o login.
     *
     * @return a senha do usuário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha para o login.
     *
     * @param senha a nova senha.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}