package com.example.securityskilltesting;


public class NotFoundExeption extends RuntimeException{
    public NotFoundExeption(String message){
        super(message);
    }
}
