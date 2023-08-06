package com.alexkinyua.contactsense.contacts;

import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */
@Controller
@RequiredArgsConstructor

public class ContactController {
    private final ContactService contactService;
    private final UserService userService;
    private final ContactRepository contactRepository;

    @GetMapping("/availableContacts")
    public String getAllContacts(Model model){
        return findPaginated(1, "id", "asc", model);
    }

    @GetMapping("/availableContacts/page/{pageNo}")
    public String findPaginated(@PathVariable(value ="pageNo") int pageNo,
                                @Param("sortField") String sortField,
                                @Param("sortDir") String sortDir,
                                Model model){
        Page<Contact> page = contactService.findPaginated(pageNo, sortField, sortDir);
        List<Contact> listContacts = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("contacts", listContacts);

        return "contactList";

    }

    @GetMapping("/createContact")
    public String createContact(Model model, Principal principal){
        model.addAttribute("contact", new Contact());
        return "createContact";
    }
    @PostMapping("/save")
    public String addContact(Contact contact){
        contactService.save(contact);
        return "redirect:/availableContacts?save_success";
    }

    @GetMapping("/editContact/{id}")
    public String editContactForm (@PathVariable("id") Long id, Model model, Principal principal){
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "editContact";
    }
    @PostMapping("updateContact/{id}")
    public String updateContact(@PathVariable("id") Long id, Contact contact){
        Contact existingContact = contactRepository.findById(id).orElseThrow();

        existingContact.setFirstName(contact.getFirstName());
        existingContact.setLastName(contact.getLastName());
        existingContact.setEmail(contact.getEmail());
        existingContact.setPhoneNumber(contact.getPhoneNumber());
        existingContact.setAddress(contact.getAddress());
        existingContact.setUserContact(contact.getUserContact());

        contactService.save(contact);

        return "redirect:/availableContacts?update_success";
    }

    @GetMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable("id") Long id){
        contactService.deleteContactById(id);
        return "redirect:/availableContacts?delete_success";
    }

    @ModelAttribute
    public void addCurrentUser(Model model, Principal principal) {
        if (principal != null && !model.containsAttribute("user")) { // Check if user is authenticated and "user" attribute is not already present
            String username = principal.getName();
            Optional<User> userOptional = userService.findByEmail(username);
            userOptional.ifPresent(user -> model.addAttribute("user", user));
        }
    }

}
