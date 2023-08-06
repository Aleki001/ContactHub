package com.alexkinyua.contactsense.contacts;

import org.springframework.data.domain.Page;

import java.util.List;

/**
  * @Author: Alex Kinyua
  */
public interface IContactService {
    void save(Contact contact);

    List<Contact> getAllContacts();
    Contact getContactById(Long id);
    void deleteContactById(Long id);
    Page<Contact> findPaginated(int pageNo, String sortField, String sortDir);

    long totalContacts();

}
