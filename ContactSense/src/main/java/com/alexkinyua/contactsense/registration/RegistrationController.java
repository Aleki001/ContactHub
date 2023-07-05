package com.alexkinyua.contactsense.registration;

import com.alexkinyua.contactsense.events.RegistrationCompleteEvent;
import com.alexkinyua.contactsense.registration.token.VerificationToken;
import com.alexkinyua.contactsense.registration.token.VerificationTokenService;
import com.alexkinyua.contactsense.user.IUserService;
import com.alexkinyua.contactsense.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
  * @Author: Alex Kinyua
  */

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;

    @GetMapping("/registration-form")
    public String ShowRegistrationForm(Model model){
        model.addAttribute("user", new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registrationRequest, HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);

        //publish the verification email event here
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));


        return "redirect:/register/registration-form?success";
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()){
            return "redirect:/login?verified";
        }

        String verificationResult = tokenService.validateToken(token);

        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        String appUrl = request.getRequestURL().toString();
        return  appUrl.replace(request.getServletPath(), "");

    }


}
