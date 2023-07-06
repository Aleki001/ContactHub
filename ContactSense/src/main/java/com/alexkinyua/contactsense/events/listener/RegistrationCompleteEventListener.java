package com.alexkinyua.contactsense.events.listener;

import com.alexkinyua.contactsense.events.RegistrationCompleteEvent;
import com.alexkinyua.contactsense.registration.token.VerificationTokenService;
import com.alexkinyua.contactsense.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
  * @Author: Alex Kinyua
  */
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final JavaMailSender mailSender;
    private final VerificationTokenService tokenService;
    private User user;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. get the newly registered user
        user = event.getUser();
        // 2. create a verification token for user
        String verificationToken = UUID.randomUUID().toString();
        // 3. save the verification token for the user
        tokenService.saveUserVerificationToken(user, verificationToken);
        // 4. build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        // 5. send the email to the user
        try {
            sendVerificationEmail(url, user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String url, User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Users Verification Service";
        String mailContent = "<p> Hi "+ user.getName()+", </p>" +
                "<p>Thank you for registering with us." + " " +
                "Please follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    public void sendPasswordResetVerificationEmail(String url, User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String senderName = "Users Verification Service";
        String mailContent = "<p> Hi "+ user.getName()+ ", </p>"+
                "<p><b>You recently requested to reset your password.</b>"+" " +
                "Please follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    private static void emailMessage(String subject, String senderName,
                                     String mailContent, JavaMailSender mailSender, User theUser)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("alexkinyua001alx@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
