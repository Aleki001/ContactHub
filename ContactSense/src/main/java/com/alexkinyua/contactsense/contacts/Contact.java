package com.alexkinyua.contactsense.contacts;

import com.alexkinyua.contactsense.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
  * @Author: Alex Kinyua
  */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String Email;
    private String address;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userContact;





}
