package com.alexkinyua.contactsense.registration;

import com.alexkinyua.contactsense.events.RegistrationCompleteEvent;
import com.alexkinyua.contactsense.events.listener.RegistrationCompleteEventListener;
import com.alexkinyua.contactsense.registration.passwordReset.IPasswordResetTokenService;
import com.alexkinyua.contactsense.registration.token.VerificationToken;
import com.alexkinyua.contactsense.registration.token.VerificationTokenService;
import com.alexkinyua.contactsense.user.IUserService;
import com.alexkinyua.contactsense.user.User;
import com.alexkinyua.contactsense.utility.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

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
    private final IPasswordResetTokenService passwordResetTokenService;
    private final RegistrationCompleteEventListener eventListener;

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
        return appUrl.replace(request.getServletPath(), "");
    }


    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm(){
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        Optional<User> user= userService.findByEmail(email);
        if (user.isEmpty()){
            return  "redirect:/register/forgot-password-request?not_found";
        }
        String passwordResetToken = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
        //send password reset verification email to the user
        String url = applicationUrl(request)+"/register/password-reset-form?token="+passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(url, user.get());
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/register/forgot-password-request?success";
    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        return "password-reset-form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")){
            return "redirect:/error?invalid_token";
        }
        Optional<User> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()){
            passwordResetTokenService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";
    }

}

