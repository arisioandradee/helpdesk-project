package com.helpdeskturmaa.helpdesk;

import java.util.Scanner;

import com.helpdeskturmaa.helpdesk.domain.enums.Perfil;


public class TestePessoa {

	public static void main(String []args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Digite o ID: ");
		Integer id = sc.nextInt();
		
		System.out.println("Digite o Nome: ");
		String nome = sc.nextLine();
		
		System.out.println("Digite o CPF: ");
		String cpf = sc.nextLine();
		
		System.out.println("Digite o Email: ");
		String email = sc.nextLine();
		
		System.out.println("Digite a Senha: ");
		String senha = sc.nextLine();
	}
	
}
