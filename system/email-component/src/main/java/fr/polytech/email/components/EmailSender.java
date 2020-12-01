package fr.polytech.email.components;

import java.util.List;

import javax.mail.MessagingException;

public interface EmailSender {
    void sendMultipleTemplateMessage(List<String> contacts, String subject, String text) throws MessagingException;
    void sendSimpleMessage(String to, String subject, String text) throws MessagingException;
    void sendTemplateMessage(String to, String subject, String text) throws MessagingException;
}
