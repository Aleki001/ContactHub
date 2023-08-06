package com.alexkinyua.contactsense.home;

import com.alexkinyua.contactsense.contacts.Contact;
import com.alexkinyua.contactsense.contacts.ContactService;
import com.alexkinyua.contactsense.home.changePassword.ChangePasswordService;
import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final ChangePasswordService changePasswordService;
    private final ContactService contactService;
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("users", userService.getUsers());

        long totalContacts = contactService.totalContacts();
        long totalUsers = userService.totalUsers();
        model.addAttribute("noOfContacts", totalContacts);
        model.addAttribute("noOfUsers", totalUsers);
        return "dashboard";
    }

    @ModelAttribute
    public void addCurrentUser(Model model, Principal principal) {
        if (principal != null && !model.containsAttribute("user")) { // Check if user is authenticated and "user" attribute is not already present
            String username = principal.getName();
            Optional<User> userOptional = userService.findByEmail(username);
            userOptional.ifPresent(user -> model.addAttribute("user", user));
        }
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/login?logout";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "accessDenied";
    }

    @GetMapping("/profile-page")
    public String showProfilePage() {
        return "profilePage";
    }

    @GetMapping("/change-password-form")
    public String changePasswordForm(Principal principal, Model model){
        return "change-password-form";
    }


    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal) {
        // 1. Retrieve the currently logged-in user's username or email from the Principal object
        String username = principal.getName();

        // 2. Implement your password change logic based on your application's requirements
        // For example, you can validate the current password, check for password complexity, and update the password

        // 3. Validate the current password
        if (!changePasswordService.isValidPassword(username, currentPassword)) {
            //model.addAttribute("error", "Invalid current password");
            return "redirect:/change-password-form?error1";
        }

        // 4. Validate the new password and confirm password
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/change-password-form?error2";
        }

        // 5. Update the password
        boolean passwordChanged = changePasswordService.changePassword(username, newPassword);

        if (passwordChanged) {
            return "redirect:/change-password-form?success";
        } else {
            return "redirect:/change-password-form?error3";
        }

    }
}
