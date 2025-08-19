package com.helpdeskturmaa.helpdesk;

import com.helpdeskturmaa.helpdesk.domain.enums.Perfil;
import com.helpdeskturmaa.helpdesk.domain.Tecnico;

public class TesteTecnico {

	public static void main(String []args) {
		Integer id = 1;
		String nome = "Bill Gates";
		String cpf = "760.457.770-93";
		String email = "bill@gmail.com";
		String senha = "123";
		Perfil perfil = Perfil.ADMIN;
		
		Tecnico tecnico = new Tecnico(id, nome, cpf, email, senha);
		
	    System.out.println("\n==== Perfil Técnico Criado ====");
	    System.out.println("ID: " + tecnico.getId());
	    System.out.println("Nome: " + tecnico.getNome());
	    System.out.println("CPF: " + tecnico.getCpf());
	    System.out.println("E-mail: " + tecnico.getEmail());
	    System.out.println("Senha: " + tecnico.getSenha());
	    System.out.println("Pefil:" + perfil);
	}
}
