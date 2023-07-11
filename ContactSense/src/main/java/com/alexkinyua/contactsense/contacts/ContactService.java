package com.alexkinyua.contactsense.contacts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  * @Author: Alex Kinyua
  */
@Service
@RequiredArgsConstructor
public class ContactService implements IContactService{
    private final ContactRepository contactRepository;
    @Override
    public void save(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(Long id) {
        return contactRepository.findById(id).get();
    }

    @Override
    public void deleteContactById(Long id) {
        contactRepository.deleteById(id);
    }
}
