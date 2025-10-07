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
    
    public void instanciaDB() {
    	
    	String senhaCriptografada = encoder.encode("123");
    	
        Tecnico tec1 = new Tecnico(null, "Bill Gates", "76045777093", "bill@mail.com", senhaCriptografada);
        tec1.addPerfil(Perfil.ADMIN);

        Cliente cli1 = new Cliente(null, "Linus Torvalds", "70511744013", "linus@mail.com", senhaCriptografada);

        Chamado cha1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "primeiro chamado", tec1, cli1);

        tecnicoRepository.saveAll(Arrays.asList(tec1));
        clienteRepository.saveAll(Arrays.asList(cli1));
        chamadoRepository.saveAll(Arrays.asList(cha1));
    }
}
