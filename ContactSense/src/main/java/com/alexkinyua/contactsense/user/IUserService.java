package com.alexkinyua.contactsense.user;


import com.alexkinyua.contactsense.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */
public interface IUserService {
    List<User> getUsers();

    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    void updateUser(Long id, String name, String email);
    void deleteUser(Long id);
}
