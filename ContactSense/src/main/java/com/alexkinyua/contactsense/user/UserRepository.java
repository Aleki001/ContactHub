package com.alexkinyua.contactsense.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
