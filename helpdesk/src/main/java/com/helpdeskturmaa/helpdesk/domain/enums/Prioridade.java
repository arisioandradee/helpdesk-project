package com.helpdeskturmaa.helpdesk.domain.enums;

public enum Prioridade {
	BAIXA(0, "ROLE_BAIXA"),
	MEDIA(1, "ROLE_MEDIA"),
	ALTA(2, "ROLE_ALTA");
	
	Integer codigo;
	String descricao;
	
	private Prioridade(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Prioridade toEnum(Integer codigo) {
		if(codigo == null) {
			return null;
		}
		
		for(Prioridade x : Prioridade.values()) {
			if(codigo.equals(x.getCodigo())) {
				return x;
			}
		}
		throw new IllegalArgumentException("Perfil Inválido");
	}
}
