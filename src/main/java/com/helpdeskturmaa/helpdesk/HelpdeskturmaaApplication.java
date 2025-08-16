package com.helpdeskturmaa.helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelpdeskturmaaApplication {


    public static void main(String[] args) {
        SpringApplication.run(HelpdeskturmaaApplication.class, args);

        System.out.println("**********************************************");
        System.out.println("*                                            *");
        System.out.println("*  APLICAÇÃO HELPDESK INICIADA COM SUCESSO! *");
        System.out.println("*                                            *");
        System.out.println("**********************************************");
    }

}
