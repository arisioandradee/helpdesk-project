package com.helpdeskturmaa.helpdesk.dto;

import java.io.Serializable;
	
import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpdeskturmaa.helpdesk.domain.Tecnico;

/**
 * Data Transfer Object (DTO) para a entidade {@link Tecnico}.
 * * Utilizado para transferência de dados do Técnico entre a camada de serviço e a camada de apresentação
 * (controladores REST).
 * * O campo {@code senha} possui uma configuração especial (WRITE_ONLY) para
 * ser aceito em requisições (ex: POST, PUT) mas omitido em serializações de resposta (ex: GET).
 * Implementa {@link Serializable} para permitir a serialização.
 */
public class TecnicoDTO implements Serializable {

    /**
     * Identificador de serialização da classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * O ID (identificador) único do técnico.
     */
    private Integer id;

    /**
     * O nome completo do técnico.
     */
    private String nome;

    /**
     * O Cadastro de Pessoa Física (CPF) do técnico.
     */
    private String cpf;

    /**
     * O endereço de e-mail do técnico, que é utilizado como login.
     */
    private String email;

    /**
     * A senha do técnico.
     * A anotação {@link JsonProperty} com {@code Access.WRITE_ONLY} garante que a senha
     * será incluída ao receber dados (deserialização) mas omitida ao enviar dados (serialização).
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;
    
    /**
     * Construtor padrão (vazio) da classe TecnicoDTO.
     */
    public TecnicoDTO() {
        super();
    }
    
    /**
     * Construtor que recebe um objeto {@link Tecnico} e o converte para TecnicoDTO.
     * * Usado para popular o DTO a partir da entidade, copiando todos os atributos,
     * inclusive a senha para garantir a consistência dos dados internos antes de serializar/deserializar.
     *
     * @param obj O objeto Tecnico original a ser convertido.
     */
    public TecnicoDTO(Tecnico obj) {
        super();
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.email = obj.getEmail();
        this.senha = obj.getSenha();
    }
    
    /**
     * Retorna o ID do técnico.
     *
     * @return o ID do técnico.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define o ID do técnico.
     *
     * @param id o novo ID do técnico.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retorna o nome do técnico.
     *
     * @return o nome do técnico.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do técnico.
     *
     * @param nome o novo nome do técnico.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CPF do técnico.
     *
     * @return o CPF do técnico.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do técnico.
     *
     * @param cpf o novo CPF do técnico.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Retorna o e-mail do técnico.
     *
     * @return o e-mail do técnico.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do técnico.
     *
     * @param email o novo e-mail do técnico.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna a senha do técnico.
     *
     * @return a senha do técnico.
     */
	public String getSenha() {
		return senha;
	}

    /**
     * Define a senha do técnico.
     *
     * @param senha a nova senha do técnico.
     */
	public void setSenha(String senha) {
		this.senha = senha;
	}
}