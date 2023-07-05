package com.alexkinyua.contactsense.registration.token;

import com.alexkinyua.contactsense.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
}
