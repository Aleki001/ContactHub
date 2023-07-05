package com.alexkinyua.contactsense.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
  * @Author: Alex Kinyua
  */

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    @GetMapping
    public String getUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        return "users";
    }
}
