package com.alexkinyua.contactsense.contacts;

import java.util.List;

/**
  * @Author: Alex Kinyua
  */
public interface IContactService {
    void save(Contact contact);
    List<Contact> getAllContacts();
    Contact getContactById(Long id);
    void deleteContactById(Long id);

}
