package com.helpdeskturmaa.helpdesk;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.domain.enums.Prioridade;
import com.helpdeskturmaa.helpdesk.domain.enums.Status;

public class TesteChamado {
    public static void main(String[] args) {

        Integer id = 3;
        Prioridade prioridade = Prioridade.MEDIA; 
        Status status = Status.ANDAMENTO;         
        String titulo = "Chamado 01";
        String observacoes = "primeiro chamado";

        // Criando o chamado
        Chamado chamado = new Chamado(id, prioridade, status, titulo, observacoes);

        // Exibindo os dados
        System.out.println("\n==== Chamado Criado ====");
        System.out.println("ID: " + chamado.getId());
        System.out.println("Título: " + chamado.getTitulo());
        System.out.println("Observações: " + chamado.getObservacoes());
        System.out.println("Data de Abertura: " + chamado.getDataAbertura());
        System.out.println("Data de Fechamento: " + chamado.getDataFechamento());
        System.out.println("Prioridade: " + chamado.getPrioridade());
        System.out.println("Status: " + chamado.getStatus());
    }
}
