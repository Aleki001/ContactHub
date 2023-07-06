package com.alexkinyua.contactsense.home.changePassword;

import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangePasswordService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public boolean isValidPassword(String username, String password) {
        // 1. Retrieve the user by username from the data store
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            return false; // User not found
        }
        User user = optionalUser.get();

        // 2. Compare the provided password with the stored password using your password hashing mechanism
        return passwordEncoder.matches(password, user.getPassword());
    }


    public boolean changePassword(String username, String newPassword) {
        // 1. Retrieve the user by username from the data store
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            return false; // User not found
        }
        User user = optionalUser.get();

        // 2. Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));

        // 3. Save the updated user
        userRepository.save(user);

        return true; // Password changed successfully
    }
}
