package com.helpdeskturmaa.helpdesk.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.domain.enums.Prioridade;
import com.helpdeskturmaa.helpdesk.domain.enums.Status;

/**
 * Data Transfer Object (DTO) para a entidade {@link Chamado}.
 * * Utilizado para transferência de dados entre a camada de serviço e a camada de apresentação
 * (controladores REST), simplificando a exposição de dados e evitando referências circulares.
 * Implementa {@link Serializable} para permitir a serialização.
 */
public class ChamadoDTO implements Serializable {

    /**
     * Identificador de serialização da classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * O ID (identificador) do chamado.
     */
    private Integer id;

    /**
     * A data de abertura do chamado.
     * Padrão de formatação: dd/MM/yyyy.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAbertura = LocalDate.now();

    /**
     * A data de fechamento do chamado.
     * Padrão de formatação: dd/MM/yyyy.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFechamento;

    /**
     * A prioridade do chamado (Baixa, Média, Alta).
     */
    private Prioridade prioridade;

    /**
     * O status atual do chamado (Aberto, Em Andamento, Encerrado).
     */
    private Status status;

    /**
     * O título descritivo do chamado.
     */
    private String titulo;

    /**
     * Observações ou descrição detalhada do problema ou solicitação.
     */
    private String observacoes;

    /**
     * O ID do Técnico associado a este chamado.
     */
    private Integer tecnico;

    /**
     * O ID do Cliente que abriu este chamado.
     */
    private Integer cliente;

    /**
     * Construtor padrão (vazio) da classe ChamadoDTO.
     */
    public ChamadoDTO() {
        super();
    }

    /**
     * Construtor que recebe um objeto {@link Chamado} e o converte para ChamadoDTO.
     * * Realiza a cópia dos atributos, incluindo a conversão das referências de
     * Técnico e Cliente para apenas seus IDs.
     *
     * @param obj O objeto Chamado original a ser convertido.
     */
    public ChamadoDTO(Chamado obj) {
        super();
        this.id = obj.getId();
        this.dataAbertura = obj.getDataAbertura();
        this.dataFechamento = obj.getDataFechamento();
        this.prioridade = obj.getPrioridade();
        this.status = obj.getStatus();
        this.titulo = obj.getTitulo();
        this.observacoes = obj.getObservacoes();

        if (obj.getTecnico() != null) {
            this.tecnico = obj.getTecnico().getId();
        }
        if (obj.getCliente() != null) {
            this.cliente = obj.getCliente().getId();
        }
    }

    /**
     * Retorna o ID do chamado.
     *
     * @return o ID do chamado.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define o ID do chamado.
     *
     * @param id o novo ID do chamado.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retorna a data de abertura do chamado.
     *
     * @return a data de abertura.
     */
    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    /**
     * Define a data de abertura do chamado.
     *
     * @param dataAbertura a nova data de abertura.
     */
    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    /**
     * Retorna a data de fechamento do chamado.
     *
     * @return a data de fechamento.
     */
    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    /**
     * Define a data de fechamento do chamado.
     *
     * @param dataFechamento a nova data de fechamento.
     */
    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    /**
     * Retorna a prioridade do chamado.
     *
     * @return a prioridade.
     */
    public Prioridade getPrioridade() {
        return prioridade;
    }

    /**
     * Define a prioridade do chamado.
     *
     * @param prioridade a nova prioridade.
     */
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * Retorna o status do chamado.
     *
     * @return o status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Define o status do chamado.
     *
     * @param status o novo status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Retorna o título do chamado.
     *
     * @return o título.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título do chamado.
     *
     * @param titulo o novo título.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Retorna as observações detalhadas do chamado.
     *
     * @return as observações.
     */
    public String getObservacoes() {
        return observacoes;
    }

    /**
     * Define as observações detalhadas do chamado.
     *
     * @param observacoes as novas observações.
     */
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    /**
     * Retorna o ID do técnico associado ao chamado.
     *
     * @return o ID do técnico.
     */
    public Integer getTecnico() {
        return tecnico;
    }

    /**
     * Define o ID do técnico associado ao chamado.
     *
     * @param tecnico o ID do técnico.
     */
    public void setTecnico(Integer tecnico) {
        this.tecnico = tecnico;
    }

    /**
     * Retorna o ID do cliente que abriu o chamado.
     *
     * @return o ID do cliente.
     */
    public Integer getCliente() {
        return cliente;
    }

    /**
     * Define o ID do cliente que abriu o chamado.
     *
     * @param cliente o ID do cliente.
     */
    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }
}