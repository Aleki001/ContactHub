package com.alexkinyua.contactsense.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    @GetMapping
    public String getUsers(Model model, Principal principal){
        String username = principal.getName();
        Optional<User> userOptional = userService.findByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        }
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @GetMapping("/editUser/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Optional<User> user = userService.findById(id);
        model.addAttribute("user", user.get());
        return "updateUser";
    }

    @PostMapping("updateUser/{id}")
    public String updateUser(@PathVariable("id") Long id, User user){
        userService.updateUser(id, user.getName(), user.getEmail());
        return "redirect:/users?update_success";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/users?delete_success";
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
