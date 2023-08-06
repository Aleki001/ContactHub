package com.alexkinyua.contactsense.contacts;

import com.alexkinyua.contactsense.exception.ResourceNotFoundException;
import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  * @Author: Alex Kinyua
  */
@Service
@RequiredArgsConstructor
public class ContactService implements IContactService{
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;


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
        return contactRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Contact with id "+ id +" does not exist"));
    }

    @Override
    public void deleteContactById(Long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public Page<Contact> findPaginated(int pageNo, String sortField, String sortDir) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize,
                sortDir.equals("asc")? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        return contactRepository.findAll(pageable);
    }

    @Override
    public long totalContacts() {
        long totalContacts = contactRepository.count();
        return totalContacts;
    }

}
