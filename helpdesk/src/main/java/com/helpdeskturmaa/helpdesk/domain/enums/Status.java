package com.helpdeskturmaa.helpdesk.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
	ABERTO(0, "Aberto"),
	ANDAMENTO(1, "Em Andamento"),
	ENCERRADO(2, "Encerrado");
	
	private Integer codigo;
	private String descricao;
	
	private Status(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	@JsonCreator 
    public static Status toEnum(Object valor) {
        if (valor == null) {
            return null;
        }

        if (valor instanceof Integer) {
            Integer codigo = (Integer) valor;
            for (Status x : Status.values()) {
                if (codigo.equals(x.getCodigo())) {
                    return x;
                }
            }
        } 

        if (valor instanceof String) {
            String strValor = (String) valor;

            for (Status x : Status.values()) {
                if (strValor.equalsIgnoreCase(x.getDescricao())) {
                    return x;
                }
            }

            try {
                return Status.valueOf(strValor.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }
        
        throw new IllegalArgumentException("Valor de Status Inválido: " + valor);
    }
}