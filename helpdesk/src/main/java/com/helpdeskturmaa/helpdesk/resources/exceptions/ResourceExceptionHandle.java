package com.helpdeskturmaa.helpdesk.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandle {

    // ObjectNotFoundException (404)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException ex,
            HttpServletRequest request) {

        StandardError error = new StandardError(
            System.currentTimeMillis(),
            HttpStatus.NOT_FOUND.value(),
            "Não Encontrado",
            ex.getMessage(),
            request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    // DataIntegrityViolationException (400)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {

        StandardError error = new StandardError(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validação",
            ex.getMessage(),
            request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // AuthorizationException (403) 
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorizationException(AuthorizationException ex, HttpServletRequest request) {

        StandardError error = new StandardError(
            System.currentTimeMillis(),
            HttpStatus.FORBIDDEN.value(), 
            "Acesso Negado (Authorization)", 
            ex.getMessage(),
            request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> internalServerError(Exception ex, HttpServletRequest request) {

        StandardError error = new StandardError(
            System.currentTimeMillis(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            "Erro Interno do Servidor", 
            "Ocorreu um erro interno e não mapeado. Contate o suporte.",
            request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}