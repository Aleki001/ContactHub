package com.alexkinyua.contactsense.registration.token;

import com.alexkinyua.contactsense.user.User;

import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */
public interface IVerificationTokenService {
    void saveUserVerificationToken(User theUser, String verificationToken);

    Optional<VerificationToken> findByToken(String token);

    String validateToken(String token);
}
