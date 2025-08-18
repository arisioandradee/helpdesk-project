package com.helpdeskturmaa.helpdesk;

import java.util.Scanner;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.domain.enums.Prioridade;
import com.helpdeskturmaa.helpdesk.domain.enums.Status;

public class TesteChamado {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==== Criando novo Chamado ====");

        System.out.print("Digite o ID: ");
        Integer id = Integer.parseInt(scanner.nextLine());

        System.out.print("Digite o título do chamado: ");
        String titulo = scanner.nextLine();

        System.out.print("Digite as observações: ");
        String observacoes = scanner.nextLine();

        System.out.println("Selecione a prioridade (BAIXA, MEDIA, ALTA): ");
        Prioridade prioridade = null;
        try {
            prioridade = Prioridade.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Prioridade inválida. Usando PRIORIDADE.MÉDIA por padrão.");
            prioridade = Prioridade.MEDIA;
        }

        System.out.println("Selecione o status (ABERTO, EM_ANDAMENTO, ENCERRADO): ");
        Status status = null;
        try {
            status = Status.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Status inválido. Usando STATUS.ABERTO por padrão.");
            status = Status.ABERTO;
        }

        Chamado chamado = new Chamado(id, prioridade, status, titulo, observacoes);

        System.out.println("\n==== Chamado Criado ====");
        System.out.println("ID: " + chamado.getId());
        System.out.println("Título: " + chamado.getTitulo());
        System.out.println("Observações: " + chamado.getObservacoes());
        System.out.println("Data de Abertura: " + chamado.getDataAbertura());
        System.out.println("Prioridade: " + chamado.getPrioridade());
        System.out.println("Status: " + chamado.getStatus());

        scanner.close();
    }
}
