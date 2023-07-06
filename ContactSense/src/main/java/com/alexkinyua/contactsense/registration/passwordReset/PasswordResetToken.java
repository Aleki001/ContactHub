package com.alexkinyua.contactsense.registration.passwordReset;

import com.alexkinyua.contactsense.registration.token.TokenExpirationTime;
import com.alexkinyua.contactsense.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
  * @Author: Alex Kinyua
  */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordResetToken(String token, User user) {
        super();
        this.token = token;
        this.expirationTime = TokenExpirationTime.getExpirationTime();
        this.user = user;
    }
}
