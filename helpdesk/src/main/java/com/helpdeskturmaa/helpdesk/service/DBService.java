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

    /**
     * Injeção do repositório para acesso a dados de Técnico.
     */
    @Autowired
    private TecnicoRepository tecnicoRepository;

    /**
     * Injeção do repositório para acesso a dados de Cliente.
     */
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Injeção do repositório para acesso a dados de Chamado.
     */
    @Autowired
    private ChamadoRepository chamadoRepository;
    
    /**
     * Injeção do codificador de senha (BCryptPasswordEncoder) para criptografia.
     */
    @Autowired
    private PasswordEncoder encoder;
    
    /**
     * Método para popular o banco de dados com entidades iniciais (Técnicos, Clientes e Chamados).
     * * Cria um usuário ADMIN, um Técnico, um Cliente e um Chamado inicial.
     * * Criptografa as senhas antes de persistir.
     */
    public void instanciaDB() {
    	
    	String senhaCriptografada = encoder.encode("123");
    	
    	Tecnico admin = new Tecnico(null, "admin", "00000000000", "admin@mail.com", senhaCriptografada);
    	admin.addPerfil(Perfil.ADMIN);
    	
        Tecnico tec1 = new Tecnico(null, "Bill Gates", "76045777093", "bill@mail.com", senhaCriptografada);
        tec1.addPerfil(Perfil.TECNICO);
        Cliente cli1 = new Cliente(null, "Linus Torvalds", "70511744013", "linus@mail.com", senhaCriptografada);
        Chamado cha1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "primeiro chamado", tec1, cli1);

        tecnicoRepository.saveAll(Arrays.asList(admin));
        tecnicoRepository.saveAll(Arrays.asList(tec1));
        clienteRepository.saveAll(Arrays.asList(cli1));
        chamadoRepository.saveAll(Arrays.asList(cha1));
    }
}