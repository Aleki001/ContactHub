package com.alexkinyua.contactsense.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = "update User u set u.name =:name," +
            " u.email =:email where u.id =:id")
    void update(String name, String email, Long id);
}
