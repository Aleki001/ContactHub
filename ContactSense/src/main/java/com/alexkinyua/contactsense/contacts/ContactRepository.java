package com.alexkinyua.contactsense.contacts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
  * @Author: Alex Kinyua
  */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
