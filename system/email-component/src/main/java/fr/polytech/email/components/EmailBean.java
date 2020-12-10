package fr.polytech.email.components;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import fr.polytech.email.errors.MessageNotSentException;

@Component
@ComponentScan("org.springframework.mail")
public class EmailBean implements EmailSender {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendTemplateMessage(String to, String subject, String text) throws MessageNotSentException {
        MimeMessage message = emailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
            helper.setFrom("noreply@polytech.unice.fr");
            helper.setTo(to); 
            helper.setSubject(subject); 
            helper.setText(text, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new MessageNotSentException();
        }
        
        
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply@baeldung.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMultipleTemplateMessage(List<String> contacts, String subject, String text)
            throws MessageNotSentException {
        for(String to : contacts) this.sendTemplateMessage(to, subject, text);
    }
    
}
