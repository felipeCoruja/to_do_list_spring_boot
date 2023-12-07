package com.felipe.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // anotação do Spring que define classes globais para tratamento de erros e exceções
public class ExceptionHandlerController{

    @ExceptionHandler(HttpMessageNotReadableException.class)// especificando que meu método é para esse tipo de exceção
    public ResponseEntity<String> handleHttpMenssageNotReadableException(HttpMessageNotReadableException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());//esta mensagem de erro esta costumizada em ModelTask setTitle()
    }
}