package com.helpdeskturmaa.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.domain.Chamado;
import com.helpdeskturmaa.helpdesk.domain.Cliente;
import com.helpdeskturmaa.helpdesk.domain.Tecnico;
import com.helpdeskturmaa.helpdesk.domain.enums.Perfil;
import com.helpdeskturmaa.helpdesk.domain.enums.Prioridade;
import com.helpdeskturmaa.helpdesk.domain.enums.Status;
import com.helpdeskturmaa.helpdesk.repositories.ChamadoRepository;
import com.helpdeskturmaa.helpdesk.repositories.ClienteRepository;
import com.helpdeskturmaa.helpdesk.repositories.TecnicoRepository;

import java.util.Arrays;

/**
 * Serviço responsável por instanciar e popular o banco de dados com dados iniciais (seeding)
 * para fins de desenvolvimento e teste.
 */
@Service
public class DBService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * Método para popular o banco de dados com entidades iniciais.
     */
    public void instanciaDB() {

        String senhaCriptografada = encoder.encode("123");

        // Tecnicos
        Tecnico admin = new Tecnico(null, "admin", "00000000000", "admin@mail.com", senhaCriptografada);
        admin.addPerfil(Perfil.ADMIN);

        Tecnico tec1 = new Tecnico(null, "Bill Gates", "76045777093", "bill@mail.com", senhaCriptografada);
        tec1.addPerfil(Perfil.TECNICO);

        Tecnico tec2 = new Tecnico(null, "Arisio", "7184712141243", "arisio@mail.com", senhaCriptografada);
        tec2.addPerfil(Perfil.TECNICO);

        Tecnico tec3 = new Tecnico(null, "Steve Jobs", "12345678901", "steve@mail.com", senhaCriptografada);
        tec3.addPerfil(Perfil.TECNICO);

        Tecnico tec4 = new Tecnico(null, "Ada Lovelace", "98765432100", "ada@mail.com", senhaCriptografada);
        tec4.addPerfil(Perfil.TECNICO);

        // Clientes
        Cliente cli1 = new Cliente(null, "Linus Torvalds", "70511744013", "linus@mail.com", senhaCriptografada);
        Cliente cli2 = new Cliente(null, "Guido van Rossum", "70511744014", "guido@mail.com", senhaCriptografada);
        Cliente cli3 = new Cliente(null, "Dennis Ritchie", "70511744015", "dennis@mail.com", senhaCriptografada);

        // Salvar Tecnicos e Clientes antes dos chamados
        tecnicoRepository.saveAll(Arrays.asList(admin, tec1, tec2, tec3, tec4));
        clienteRepository.saveAll(Arrays.asList(cli1, cli2, cli3));

        // Chamados
        Chamado cha1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Erro ao acessar VPN", "primeiro chamado", tec1, cli1);
        Chamado cha2 = new Chamado(null, Prioridade.ALTA, Status.ANDAMENTO, "Sistema lento no login", "segundo chamado", tec1, cli2);
        Chamado cha3 = new Chamado(null, Prioridade.BAIXA, Status.ANDAMENTO, "Solicitação de instalação de software", "terceiro chamado", tec2, cli1);
        Chamado cha4 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Impressora não imprime", "quarto chamado", tec3, cli2);
        Chamado cha5 = new Chamado(null, Prioridade.MEDIA, Status.ENCERRADO, "Recuperação de senha de e-mail", "quinto chamado", tec4, cli3);
        Chamado cha6 = new Chamado(null, Prioridade.BAIXA, Status.ENCERRADO, "Atualização de antivírus", "sexto chamado", tec2, cli3);
        Chamado cha7 = new Chamado(null, Prioridade.ALTA, Status.ANDAMENTO, "Erro crítico no servidor de banco de dados", "setimo chamado", tec3, cli1);
        Chamado cha8 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Solicitação de acesso a pasta compartilhada", "oitavo chamado", tec4, cli2);
        Chamado cha9 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Problema de conexão Wi-Fi", "nono chamado", tec4, cli1);


        // Salvar chamados
        chamadoRepository.saveAll(Arrays.asList(cha1, cha2, cha3, cha4, cha5, cha6, cha7, cha8, cha9));
    }
}
