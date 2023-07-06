package com.alexkinyua.contactsense.registration.passwordReset;

import com.alexkinyua.contactsense.user.User;

import java.util.Optional;

public interface IPasswordResetTokenService {
    void createPasswordResetTokenForUser(User user, String passwordResetToken);
    String validatePasswordResetToken(String theToken);
    void resetPassword(User theUser, String password);
    Optional<User> findUserByPasswordResetToken(String theToken);
}
