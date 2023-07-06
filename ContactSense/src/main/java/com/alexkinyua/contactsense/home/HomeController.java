package com.alexkinyua.contactsense.home;

import com.alexkinyua.contactsense.home.changePassword.ChangePasswordService;
import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.user.UserService;
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
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final ChangePasswordService changePasswordService;
    @GetMapping
    public String homePage(){
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

//    @GetMapping("/profile-page")
//    public String showProfilePage(Principal principal, Model model){
//        String email = principal.getName();
//        Optional<User> user = userService.findByEmail(email);
//        model.addAttribute("user", user);
//        return "profilePage";
//    }
@GetMapping("/profile-page")
public String showProfilePage(Principal principal, Model model) {
    String email = principal.getName();
    Optional<User> optionalUser = userService.findByEmail(email);
    if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        model.addAttribute("user", user);
    } else {
        // Handle the case where the user is not found
        // You can redirect to an error page or display a message
    }
    return "profilePage";
}


    @GetMapping("/change-password-form")
    public String changePasswordForm(){
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
