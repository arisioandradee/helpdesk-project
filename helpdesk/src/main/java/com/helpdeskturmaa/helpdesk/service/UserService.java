package com.helpdeskturmaa.helpdesk.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.helpdeskturmaa.helpdesk.security.UserSS;

/**
 * Serviço de utilidade para manipulação do contexto de segurança do Spring Security.
 * * Fornece métodos estáticos para acessar informações do usuário autenticado a
 * partir de qualquer ponto da aplicação.
 */
@Service
public class UserService {

    /**
     * Retorna o usuário autenticado (logado) no sistema.
     * * Acessa o contexto de segurança do Spring Security ({@link SecurityContextHolder})
     * e tenta obter o principal (o usuário logado).
     *
     * @return O objeto {@link UserSS} que representa o usuário autenticado, ou {@code null}
     * se não houver um usuário autenticado ou em caso de exceção.
     */
    public static UserSS authenticated() {
        try {
            return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}