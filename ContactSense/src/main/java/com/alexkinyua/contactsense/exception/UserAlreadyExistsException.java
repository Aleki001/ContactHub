package com.alexkinyua.contactsense.exception;

/**
  * @Author: Alex Kinyua
  */
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
