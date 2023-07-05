package com.alexkinyua.contactsense.registration.token;

import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */
@Service
@RequiredArgsConstructor
public class VerificationTokenService implements IVerificationTokenService{
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;


    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public String validateToken(String token) {
        Optional<VerificationToken> theToken = tokenRepository.findByToken(token);
        if(theToken.isEmpty()){
            return "INVALID";
        }

        User user = theToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if((theToken.get().getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            return "EXPIRED";
        }

        user.setEnabled(true);
        userRepository.save(user);

        return "VALID";
    }
}
