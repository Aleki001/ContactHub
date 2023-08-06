package com.alexkinyua.contactsense.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
  * @Author: Alex Kinyua
  */
@Data
@RequiredArgsConstructor
public class ErrorDetails {
    private Date timeStamp;
    private String message;
    private String details;

}
