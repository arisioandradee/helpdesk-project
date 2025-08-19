package com.helpdeskturmaa.helpdesk;

import com.helpdeskturmaa.helpdesk.domain.enums.Perfil;
import com.helpdeskturmaa.helpdesk.domain.Cliente;

public class TesteCliente {

    public static void main(String[] args) {
        Integer id = 2;
        String nome = "Linus Torvalds";
        String cpf = "705.117.440-13";
        String email = "linus@mail.com";
        String senha = "123";
        Perfil perfil = Perfil.CLIENTE;

        Cliente cli1 = new Cliente(id, nome, cpf, email, senha);
        cli1.addPerfil(perfil);

        System.out.println("\n==== Cliente Criado ====");
        System.out.println("ID: " + cli1.getId());
        System.out.println("Nome: " + cli1.getNome());
        System.out.println("CPF: " + cli1.getCpf());
        System.out.println("E-mail: " + cli1.getEmail());
        System.out.println("Senha: " + cli1.getSenha());
        System.out.println("Perfis: " + cli1.getPerfis());
        System.out.println("Data Criação: " + cli1.getDataCriacao());
    }
}
