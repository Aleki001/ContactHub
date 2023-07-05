package com.alexkinyua.contactsense.registration;

import com.alexkinyua.contactsense.Role;
import lombok.Data;

import java.util.List;

/**
  * @Author: Alex Kinyua
  */
@Data
public class RegistrationRequest {
    private String name;
    private String email;
    private String password;
    private List<Role> roles;
}
