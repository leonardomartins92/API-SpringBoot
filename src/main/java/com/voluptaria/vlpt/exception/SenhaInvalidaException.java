package com.voluptaria.vlpt.exception;

public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException(){
        super("Senha inválida");
    }
}
