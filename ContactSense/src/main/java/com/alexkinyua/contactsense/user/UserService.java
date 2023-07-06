package com.alexkinyua.contactsense.user;

import com.alexkinyua.contactsense.Role;
//import com.alexkinyua.contactsense.exception.UserAlreadyExistsException;
import com.alexkinyua.contactsense.registration.RegistrationRequest;
import com.alexkinyua.contactsense.registration.token.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
//        Optional<User> user = this.findByEmail(request.getEmail());
//        if (user.isPresent()) {
//            throw new UserAlreadyExistsException(
//                    "User with email " + request.getEmail() + " already exists");
//        }
        var newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(Arrays.asList(new Role("ROLE_USER")));

        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }


}