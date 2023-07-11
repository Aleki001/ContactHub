package com.alexkinyua.contactsense.contacts;

import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.user.UserService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;
    private final UserService userService;
    @GetMapping("/availableContacts")
    public String getAllContacts(Model model, Principal principal){
        String username = principal.getName();
        Optional<User> userOptional = userService.findByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("contacts", contactService.getAllContacts());
            model.addAttribute("user", user);

        }
        return "contactList";
    }
    @GetMapping("/createContact")
    public String createContact(Model model, Principal principal){
        String username = principal.getName();
        Optional<User> userOptional = userService.findByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        }
        model.addAttribute("contact", new Contact());
        return "createContact";
    }
    @PostMapping("/save")
    public String addContact(Contact contact){
        contactService.save(contact);
        return "redirect:/contacts/availableContacts?save_success";
    }

    @GetMapping("/editContact/{id}")
    public String editContactForm (@PathVariable("id") Long id, Model model, Principal principal){
        String username = principal.getName();
        Optional<User> userOptional = userService.findByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        }
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "editContact";
    }
    @PostMapping("updateContact/{id}")
    public String updateContact(@PathVariable("id") Long id, Contact contact){
        contactService.save(contact);
        return "redirect:/contacts/availableContacts?update_success";
    }

    @GetMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable("id") Long id){
        contactService.deleteContactById(id);
        return "redirect:/contacts/availableContacts?delete_success";
    }
}
